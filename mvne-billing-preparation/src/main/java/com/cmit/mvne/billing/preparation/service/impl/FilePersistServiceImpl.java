package com.cmit.mvne.billing.preparation.service.impl;

import com.cmit.mvne.billing.preparation.common.ErrorCodeStatus;
import com.cmit.mvne.billing.preparation.common.ProcessPathProperties;
import com.cmit.mvne.billing.preparation.entity.CdrError;
import com.cmit.mvne.billing.preparation.entity.CdrFile;
import com.cmit.mvne.billing.preparation.service.*;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import com.cmit.mvne.billing.preparation.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 将话单文件持久化至数据库
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/16
 */

@Service
@Slf4j
public class FilePersistServiceImpl implements FilePersistService {

    @Autowired
    private ProcessPathProperties pathProperties;

    @Autowired
    private CdrGprsRatingService cdrGprsRatingService;
    @Autowired
    private CdrGprsSettleService cdrGprsSettleService;
    @Autowired
    private CdrSmsRatingService cdrSmsRatingService;
    @Autowired
    private CdrSmsSettleService cdrSmsSettleService;
    @Autowired
    private CdrMmsRatingService cdrMmsRatingService;
    @Autowired
    private CdrMmsSettleService cdrMmsSettleService;
    @Autowired
    private CdrGsmRatingService cdrGsmRatingService;
    @Autowired
    private CdrGsmSettleService cdrGsmSettleService;

    @Autowired
    private CdrDupCheckService dupCheckService;

    @Autowired
    private CdrErrorService cdrErrorService;

    @Autowired
    @Qualifier("redisTemplateTrans")
    private RedisTemplate<String, Object> redisTemplateTrans;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(CdrFile cdrFile) throws IOException {

        // 保存流量话单
        cdrGprsRatingService.saveBatch(cdrFile.getCdrGprsRatingList());
        cdrGprsSettleService.saveBatch(cdrFile.getCdrGprsSettleList());

        // 保存短信话单
        cdrSmsRatingService.saveBatch(cdrFile.getCdrSmsRatingList());
        cdrSmsSettleService.saveBatch(cdrFile.getCdrSmsSettleList());

        // 保存彩信话单
        cdrMmsRatingService.saveBatch(cdrFile.getCdrMmsRatingList());
        cdrMmsSettleService.saveBatch(cdrFile.getCdrMmsSettleList());

        // 保存语音话单
        cdrGsmRatingService.saveBatch(cdrFile.getCdrGsmRatingList());
        cdrGsmSettleService.saveBatch(cdrFile.getCdrGsmSettleList());

        List<CdrError> cdrErrorList = getCdrErrorList(cdrFile);
        cdrErrorService.saveBatch(cdrErrorList);

        // 保存错单
        if(cdrFile.getErrorCdrList().size() > 0) {
            File errorFile = new File(pathProperties.getErrorPath() + "Severe" + cdrFile.getOriginFile().getName());
            FileUtil.writeToFile(errorFile, cdrFile.getErrorCdrList());
            log.info("Save file {} success!", errorFile.getAbsolutePath());
        }

        // 保存成功处理话单
        if(cdrFile.getSuccessCdrList().size() > 0) {
            File successFile = new File(pathProperties.getSuccessPath() + cdrFile.getOriginFile().getName() + ".success");
            FileUtil.writeToFile(successFile, cdrFile.getSuccessCdrList());
            log.info("Save file {} success!", successFile.getAbsolutePath());
        }

        // 删除redo
        dupCheckService.deleteRedoLog(cdrFile.getOriginFile().getName());
        dupCheckService.deleteDupLog(cdrFile.getOriginFile().getName());

        // 处理完文件后，最后一步，才写文件的查重键——filename:文件名
        // 这样只有完全处理完的文件才会被批重单
        final String key = "filename:" + cdrFile.getOriginFile().getName();
        redisTemplateTrans.opsForValue().set(key, cdrFile.getOriginFile().getName(), 30, TimeUnit.DAYS);
    }

    private List<CdrError> getCdrErrorList(CdrFile cdrFile) {

        List<CdrError> cdrErrorList = new ArrayList<>();
        String originFile = cdrFile.getOriginFile().getName();
        // 话单接收时间
        Date fileReceiveDate = DateTimeUtil.getDateofTimestamp(cdrFile.getWorkFile().lastModified());

        for(String cdrStr : cdrFile.getErrorCdrList()) {
            CdrError cdrError = new CdrError();

            String[] splitCdr = cdrStr.split(CdrParsingServiceImpl.cdrSplit, -1);

            cdrError.setOriginFile(originFile);
            cdrError.setErrorCode(splitCdr[0]);
            cdrError.setLineNumber(Long.parseLong(splitCdr[1]));
            cdrError.setReceiveTime(fileReceiveDate);
            cdrError.setCdrDetail(cdrStr.substring(cdrStr.indexOf("|", cdrStr.indexOf("|") + 1) + 1));
            cdrError.setStatus(ErrorCodeStatus.INITIAL);
            cdrErrorList.add(cdrError);
        }
        return cdrErrorList;
    }


//    private String tranformArrayListToString(List<String> errorCdrsList) {
//        StringBuilder sb = new StringBuilder();
//        for(String errorCdr : errorCdrsList) {
//            sb.append(errorCdr).append("\n");
//        }
//        return sb.toString();
//    }
//
//    private List<CdrGsmSettle> transformToCdrGsmSettle(List<CdrGsmRating> cdrGsmRatingList) {
//        List<CdrGsmSettle> cdrGsmSettleList = new ArrayList<>();
//        for(CdrGsmRating cdrGsmRating : cdrGsmRatingList) {
//            CdrGsmSettle cdrGsmSettle = new CdrGsmSettle();
//
//            BeanUtils.copyProperties(cdrGsmRating, cdrGsmSettle);
//            cdrGsmSettleList.add(cdrGsmSettle);
//        }
//        return cdrGsmSettleList;
//    }

//    private List<CdrGsmRating> transformToCdrGsmRating(List<CdrGsm> cdrGsmList) {
//        List<CdrGsmRating> cdrGsmRatingList = new ArrayList<>();
//        for(CdrGsm cdrGsm : cdrGsmList) {
//            CdrGsmRating cdrGsmRating = new CdrGsmRating();
//            BeanUtils.copyProperties(cdrGsm, cdrGsmRating);
//            cdrGsmRatingList.add(cdrGsmRating);
//        }
//        return cdrGsmRatingList;
//    }
//
//    private List<CdrMmsSettle> transformToCdrMmsSettle(List<CdrMmsRating> cdrMmsRatingList) {
//        List<CdrMmsSettle> cdrMmsSettleList = new ArrayList<>();
//        for(CdrMmsRating cdrMmsRating : cdrMmsRatingList) {
//            CdrMmsSettle cdrMmsSettle = new CdrMmsSettle();
//
//            BeanUtils.copyProperties(cdrMmsRating, cdrMmsSettle);
//            cdrMmsSettleList.add(cdrMmsSettle);
//        }
//        return cdrMmsSettleList;
//    }
//
//    private List<CdrMmsRating> transformToCdrMmsRating(List<CdrMms> cdrMmsList) {
//        List<CdrMmsRating> cdrMmsRatingList = new ArrayList<>();
//        for(CdrMms cdrMms : cdrMmsList) {
//            CdrMmsRating cdrMmsRating = new CdrMmsRating();
//            BeanUtils.copyProperties(cdrMms, cdrMmsRating);
//            cdrMmsRatingList.add(cdrMmsRating);
//        }
//        return cdrMmsRatingList;
//    }
//
//    private List<CdrSmsSettle> transformToCdrSmsSettle(List<CdrSmsRating> cdrSmsRatingList) {
//        List<CdrSmsSettle> cdrSmsSettleList = new ArrayList<>();
//        for(CdrSmsRating cdrSmsRating : cdrSmsRatingList) {
//            CdrSmsSettle cdrSmsSettle = new CdrSmsSettle();
//
//            BeanUtils.copyProperties(cdrSmsRating, cdrSmsSettle);
//            cdrSmsSettleList.add(cdrSmsSettle);
//        }
//        return cdrSmsSettleList;
//    }
//
//
//    private List<CdrSmsRating> transformToCdrSmsRating(List<CdrSms> cdrSmsList) {
//        List<CdrSmsRating> smsRatingList = new ArrayList<>();
//        for(CdrSms sms : cdrSmsList) {
//            CdrSmsRating smsRating = new CdrSmsRating();
//            BeanUtils.copyProperties(sms, smsRating);
//            smsRatingList.add(smsRating);
//        }
//        return smsRatingList;
//    }

//    private List<CdrGprsRating> transformToCdrGprsRating(List<CdrGprs> cdrGprsList) {
//        List<CdrGprsRating> gprsRatingList = new ArrayList<>();
//        for(CdrGprs gprs : cdrGprsList) {
//            CdrGprsRating  gprsRating = new CdrGprsRating();
//            BeanUtils.copyProperties(gprs, gprsRating);
//            gprsRatingList.add(gprsRating);
//        }
//        return gprsRatingList;
//    }
//    private List<CdrGprsSettle> transformToCdrGprsSettle(List<CdrGprsRating> cdrGprsRatingList) {
//        List<CdrGprsSettle> gprsSettleList = new ArrayList<>();
//        for(CdrGprs gprs : cdrGprsRatingList) {
//            CdrGprsSettle gprsSettle = new CdrGprsSettle();
//
//            BeanUtils.copyProperties(gprs, gprsSettle);
//            gprsSettleList.add(gprsSettle);
//        }
//        return gprsSettleList;
//    }

    public static void main(String[] args) {
        String test = "111|rer|ffdf|4343";
        System.out.println(test.indexOf("|"));
        System.out.println(test.indexOf("|", test.indexOf("|") + 1));
        System.out.println(test.substring(0, test.indexOf("|")));
        System.out.println(test.substring(test.indexOf("|")+1, test.indexOf("|", test.indexOf("|")+1)));
    }
}
