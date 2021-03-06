package com.cmit.mvne.billing.rating.gprs.handler;

import com.cmit.mvne.billing.rating.gprs.common.*;
import com.cmit.mvne.billing.rating.gprs.config.MyBatisPlusConfig;
import com.cmit.mvne.billing.rating.gprs.config.SystemConfiguration;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditControl;
import com.cmit.mvne.billing.rating.gprs.service.GprsRatingService;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprsError;
import com.cmit.mvne.billing.user.analysis.service.CdrGprsErrorService;
import com.cmit.mvne.billing.user.analysis.service.CdrGprsService;
import com.cmit.mvne.billing.user.analysis.service.ErrCdrGprsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.cmit.mvne.billing.rating.gprs.common.RedoStatus.REDO_NORMAL_STATUS;
import static com.cmit.mvne.billing.rating.gprs.common.ReratStatus.RERAT_NORMAL_STATUS;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/29
 */
@Slf4j
public class RatingGprsHandler {

    @Autowired
    private GprsRatingService gprsRatingService;
    @Autowired
    private CdrGprsService cdrGprsService;
    @Autowired
    private CdrGprsErrorService cdrGprsErrorService;
    @Autowired
    private ErrCdrGprsService errCdrGprsService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    SystemConfiguration systemConfiguration;
    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    MyBatisPlusConfig myBatisPlusConfig;
    @Autowired
    CreditControl creditControl;


    @DependsOn({"bureauCacheManager", "bureauCacheService"})
    public List<CdrGprs> handleCdr(List<CdrGprs> cdrGprsList) throws MvneException, InterruptedException {

        HashMap<String, List<CdrGprs>> successMap = new HashMap<>();
        List<CdrGprsError> errorList = new ArrayList<>();
        List<String> expireList = new ArrayList<>();
        List<CreditCallInfo> creditCallInfoList = new ArrayList<>();

        log.info("Total num of this scan is: {}", cdrGprsList.size());
        for (CdrGprs cdrGprs : cdrGprsList) {
           /* DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);*/
            expireList.add(cdrGprs.getOriginalFile());
            CdrGprsError cdrGprsError = new CdrGprsError(cdrGprs);
            // ????????????catch??????????????????????????????
            //try {
            RatingError ratingError = gprsRatingService.ratingGprs(cdrGprs, successMap, errorList, creditCallInfoList, ReadFlag.RATING_FLAG);
            String lockKey = "InfoManageKey:" + cdrGprs.getMsisdn();
            distributeLock.unlock(lockKey);
            // ????????????null??????????????????successList????????????????????????
            if (ratingError.isNotNull()) {
                // ?????????????????????????????????????????????
                cdrGprsError.setUserId(cdrGprs.getUserId());
                //cdrGprsError.setFinishTime(new Date());
                cdrGprsError.setErrCode(ratingError.getErrCode());
                cdrGprsError.setErrDesc(ratingError.getErrDesc());
                cdrGprsError.setRedoFlag(REDO_NORMAL_STATUS);
                cdrGprsError.setReratFlag(RERAT_NORMAL_STATUS);
                cdrGprsError.setFinishTime(new Date());
                log.info("Rating Error, Error Cdr : {} ", cdrGprsError.toJsonString());
                errorList.add(cdrGprsError);
                RedoLogWriter.writeRedoLog(cdrGprsError.getOriginalFile(), cdrGprsError.getId(), cdrGprsError.toJsonString());
            }

            //transactionManager.commit(status);
        }

        // ???????????????????????????????????????????????????
        loadSuccessCdr(successMap);
        loadFailedCdr(errorList);
        // ??????????????????
        try {
            callCreditControl(creditCallInfoList);
        } catch (Exception e) {
            log.error("Call credit info fail!", e);
        }

        for (String fileName : expireList) {
            String redoKey = "RatingRedo:" + fileName;
            // 31??????????????????????????????
            redisTemplate.expire(redoKey, systemConfiguration.getRatingRedoExpireDays(), TimeUnit.DAYS);
        }

        return cdrGprsList;
    }

    private void callCreditControl(List<CreditCallInfo> creditCallInfoList) throws MvneException {
        creditControl.callList(creditCallInfoList);
    }

    public void handleErrorMessage(MessagingException messagingException) {
        log.error(messagingException.getFailedMessage().getPayload().toString());
        log.error("MessageException : ", messagingException.getMessage(), messagingException);

    }

    protected void loadSuccessCdr(HashMap<String, List<CdrGprs>> successMap) {
        for (String s : successMap.keySet()) {
            log.info("Date {}, Successful cdrs: {}.", s, successMap.get(s).size());
            MyBatisPlusConfig.tableNameHolder.set(s);
            cdrGprsService.saveBatch(successMap.get(s));
        }
        //cdrGprsService.saveBatch(successMap);

    }

    protected void loadFailedCdr(List<CdrGprsError> errorList) {
        log.info("Failed cdrs: {}.", errorList.size());
        cdrGprsErrorService.saveBatch(errorList);
    }
}
