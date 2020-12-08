package com.cmit.mvne.billing.settle.service.impl;

import com.cmit.mvne.billing.settle.common.SettleErrorCode;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprs;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprsError;
import com.cmit.mvne.billing.settle.service.GprsSettleService;
import com.cmit.mvne.billing.settle.service.SettleCdrGprsErrorService;
import com.cmit.mvne.billing.settle.service.SettleCdrGprsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 结算服务类
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/8
 */
@Slf4j
@Service
public class GprsSettleServiceImpl implements GprsSettleService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SettleCdrGprsService settleCdrGprsService;
    @Autowired
    private SettleCdrGprsErrorService settleCdrGprsErrorService;

    /**
     * 话单检错
     * @param msisdn 结算话单 号码
     * @return 返回检错后的话单
     */
    @Override
    public SettleErrorCode verify(String msisdn) {

        // 查询是否由用户资料 目前校验放在预处理，暂时不校验
//        List<CmUserDetail> cmUserDetailList = cmUserDetailService.findAllByMsisdn(msisdn);
//        if (cmUserDetailList.size() == 0) {
//            return SettleErrorCode.USER_NOT_EXIST;
//        }
        return null;

    }


    /**
     * 将话单入库
     * @param gprsList 正确话单
     * @param gprsErrorList 错误话单
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(List<SettleCdrGprs> gprsList, List<SettleCdrGprsError> gprsErrorList) {
        settleCdrGprsService.saveBatchWithTableName(gprsList);
        settleCdrGprsErrorService.saveBatch(gprsErrorList);
    }

//    /**
//     * 生成结算账期
//     * 1.	如果通话时间和发送文件时间月份相同，账期为发送文件月份时间。
//     * 2.	如果通话时间相对于发送时间是上月（或者上上月），且目前时间为本月3号之前，则账期为上月。
//     * 3.	如果通话时间相对于发送时间是为上月（或者上上月），且目前时间为本月3号之后，则账期为本月。
//     * @param eventTime 通话时间
//     * @param receiveTime Siminn 发送文件时间
//     * @return 账期
//     */
//    @Override
//    public String generateInvoicingPeriod(LocalDateTime eventTime, LocalDateTime receiveTime) {
//
//        DateTimeFormatter yyyyMMFormatter = DateTimeFormatter.ofPattern("yyyyMM");
//        if(eventTime.getMonth().equals(receiveTime.getMonth())) {
//            return receiveTime.format(yyyyMMFormatter);
//        } else {
//            LocalDateTime number3OfMonth = LocalDateTime.of(receiveTime.getYear(), receiveTime.getMonth(), 3, 0, 0);
//            // 3号之后
//            if(receiveTime.isAfter(number3OfMonth)) {
//                return receiveTime.format(yyyyMMFormatter);
//            } else {
//                return receiveTime.minusMonths(1).format(yyyyMMFormatter);
//            }
//        }
//    }

    @Override
    public String generateInvoicingPeriod(LocalDateTime receiveTime) {

        DateTimeFormatter yyyyMMFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        return receiveTime.format(yyyyMMFormatter);
    }

    public static void main(String[] args) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyyMM");

        LocalDateTime endTime = LocalDateTime.parse("20200331220900", dateTimeFormatter);
        LocalDateTime startTime = LocalDateTime.parse("20200215220900", dateTimeFormatter);

        LocalDateTime localDateTime = endTime.minusDays(30);

        System.out.println(localDateTime.format(dateTimeFormatter2));

        System.out.println(localDateTime.isBefore(startTime));

        System.out.println(endTime.minusMonths(1).format(dateTimeFormatter));



    }


}
