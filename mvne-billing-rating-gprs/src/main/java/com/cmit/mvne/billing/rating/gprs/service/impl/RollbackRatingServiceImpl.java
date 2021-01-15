package com.cmit.mvne.billing.rating.gprs.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cmit.mvne.billing.rating.gprs.service.RollbackRatingService;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.cmit.mvne.billing.rating.gprs.common.ReratStatus.*;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/14 17:33
 */

@Slf4j
@Service
public class RollbackRatingServiceImpl implements RollbackRatingService {
    @Autowired
    ApsBalanceFeeService apsBalanceFeeService;
    @Autowired
    ApsFreeResService apsFreeResService;
    @Autowired
    RatingCdrGprsReratService ratingCdrGprsReratService;

    final BigDecimal ZERO = new BigDecimal(0);

    final int RETRY_TIME = 3;
    @Value(value = "${yellow-mobile.redis.redisson.try-interval}")
    int tryInterval;

    /**
     * 回退详单的免费资源及余额
     * 必须开启事务，并通过修改详单的状态记录完成了哪些详单的回退
     * @param ratingCdrGprsReratList
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public List<RatingCdrGprsRerat> rollbackGprs(List<RatingCdrGprsRerat> ratingCdrGprsReratList, HashMap<String, Integer> resultMap) throws InterruptedException, MvneException {
        List<RatingCdrGprsRerat> failList = new ArrayList<>();
        List<RatingCdrGprsRerat> successList = new ArrayList<>();
        String BALANCE = "BalanceFail";
        String FREERES = "FreeresFail";
        if (!resultMap.containsKey(BALANCE)) {
            resultMap.put(BALANCE, 0);
        }
        if (!resultMap.containsKey(FREERES)) {
            resultMap.put(FREERES, 0);
        }

        for (RatingCdrGprsRerat ratingCdrGprsRerat : ratingCdrGprsReratList) {
            Boolean isUpdateBalanceFee = false;
            Boolean isUpdateFreeres = false;

            ApsBalanceFee apsBalanceFee = null;
            ApsFreeRes apsFreeRes = null;

            // 根据详单信息查询余额信息，并回退
            BigDecimal rollbackBalanceFee = null;
            BigDecimal fee1 = ratingCdrGprsRerat.getFee1();
            if (ZERO.compareTo(fee1) != 0) {
                // 初始化重试次数
                int retry = 0;

                // 如果使用乐观锁
                do {
                    apsBalanceFee = getBalanceFee(ratingCdrGprsRerat.getUserId());
                    if (apsBalanceFee==null) {
                        break;
                    }
                    rollbackBalanceFee = apsBalanceFee.getRemainFee().add(fee1);
                    apsBalanceFee.setRemainFee(rollbackBalanceFee);
                    // 唯一主键，可以使用updateById
                    isUpdateBalanceFee = apsBalanceFeeService.updateById(apsBalanceFee);
                    if (!isUpdateBalanceFee) {
                        retry++;
                        Thread.sleep(tryInterval);
                    }
                } while ((retry < RETRY_TIME) && !isUpdateBalanceFee);

                // 如果回退失败，应该修改rerat_flag状态，留在rerat表重新填写条件处理
                if (!isUpdateBalanceFee) {
                    ratingCdrGprsRerat.setReratFlag(RERAT_ROLLBACK_BALANCE_FAIL);
                    ratingCdrGprsRerat.setReratTime(new Date());
                    failList.add(ratingCdrGprsRerat);
                    // 已经初始化了
                    resultMap.put(BALANCE, resultMap.get(BALANCE) + 1);

                    log.info("Rollback balance fail!");
                    continue;
                }
            }
            log.info("Rollbacked balance: '{}'", fee1);

            // 根据详单信息查询免费资源信息，并回退
            BigDecimal rollbackFreeres = null;
            BigDecimal deductFreeres = ratingCdrGprsRerat.getDeductFreeres();
            if (ZERO.compareTo(deductFreeres) != 0) {
                // 初始化
                int retry = 0;

                // 如果使用乐观锁
                do {
                    apsFreeRes = getFreeRes(ratingCdrGprsRerat.getUserId(), ratingCdrGprsRerat.getProductInsId(), ratingCdrGprsRerat.getItemId());
                    if (apsFreeRes==null) {
                        // 如果回退免费资源失败，之前费用回退应该回滚
                        apsBalanceFee = getBalanceFee(ratingCdrGprsRerat.getUserId());
                        apsBalanceFee.setRemainFee(fee1);
                        // 如果回滚之前的回退失败，直接回滚整个回退事务
                        if (apsBalanceFeeService.updateById(apsBalanceFee)) {
                            break;
                        } else {
                            throw new MvneException("Failed to rollback freeres, and failed to return balancefee!");
                        }
                    }
                    // 如果用户当时没有这个场景的免费资源，那必定是扣的余额，前面已判断非0
                    rollbackFreeres = apsFreeRes.getUsedValue().subtract(deductFreeres);
                    apsFreeRes.setUsedValue(rollbackFreeres);
                    // 非唯一主键.lambda不可复用，做成局部变量
                    LambdaUpdateWrapper<ApsFreeRes> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(ApsFreeRes::getUserId, apsFreeRes.getUserId())
                            .eq(ApsFreeRes::getProductInsId, apsFreeRes.getProductInsId())
                            .eq(ApsFreeRes::getItemId, apsFreeRes.getItemId());
                    isUpdateFreeres = apsFreeResService.update(apsFreeRes, lambdaUpdateWrapper);
                    if (!isUpdateFreeres) {
                        retry++;
                        Thread.sleep(tryInterval);
                    }
                } while ((retry < RETRY_TIME) && !isUpdateFreeres);

                if (!isUpdateFreeres) {
                    ratingCdrGprsRerat.setReratFlag(RERAT_ROLLBACK_FREERES_FAIL);
                    ratingCdrGprsRerat.setReratTime(new Date());
                    failList.add(ratingCdrGprsRerat);
                    // 已经初始化了
                    resultMap.put(FREERES, resultMap.get(FREERES) + 1);

                    log.info("Rollback freeres fail!");
                    continue;
                }
            }
            log.info("Rollbacked freeres: '{}'", deductFreeres);

            // 如果没有进入continue，则更新rerat_flag状态为正常回退
            ratingCdrGprsRerat.setReratFlag(RERAT_NORMAL_ROLLBACK_STATUS);
            successList.add(ratingCdrGprsRerat);
        }

        // 回滚失败的，更新重批表状态
        if (failList.size()!=0) {
            ratingCdrGprsReratService.updateBatchById(failList);
        }

        return successList;
    }

    private ApsBalanceFee getBalanceFee(Long userid) {
        ApsBalanceFee apsBalanceFee = apsBalanceFeeService.selectByUserId(userid);
        return apsBalanceFee;
    }

    private ApsFreeRes getFreeRes(Long userId, Long productInsId, Long itemId) {
        ApsFreeRes apsFreeRes = apsFreeResService.selectByKey(userId, productInsId, itemId);
        return apsFreeRes;
    }
}
