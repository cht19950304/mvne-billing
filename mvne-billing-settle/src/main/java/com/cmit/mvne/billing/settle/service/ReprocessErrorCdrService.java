//package com.cmit.mvne.billing.settle.service;
//
//import com.cmit.mvne.billing.settle.entity.SettleCdrGprsError;
//import com.cmit.mvne.billing.settle.entity.SettleCdrSmsError;
//import com.cmit.mvne.billing.settle.entity.dto.SettleCdrGprsErrorDTO;
//import com.cmit.mvne.billing.settle.entity.dto.SettleCdrSmsErrorDTO;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
///**
// * 结算重处理服务类
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2020/6/8
// */
//public interface ReprocessErrorCdrService {
//
//    List<SettleCdrGprsErrorDTO> getSettleGprsErrorCdrs(String filename, String status, String msisdn, String errorCode, Long startTime, Long endTime, Pageable pageable);
//
//    List<SettleCdrSmsErrorDTO> getSettleSmsErrorCdrs(String filename, String status, String msisdn, String errorCode, Long startTime, Long endTime, Pageable pageable);
//
//    void reprocessGprs(List<Long> idList);
//
////    void reprocessSms(List<Long> idList);
//}
