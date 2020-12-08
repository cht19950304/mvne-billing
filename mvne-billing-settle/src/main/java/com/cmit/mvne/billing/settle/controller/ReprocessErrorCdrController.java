//package com.cmit.mvne.billing.settle.controller;
//
//import com.cmit.mvne.billing.settle.common.ServerResponse;
//import com.cmit.mvne.billing.settle.entity.dto.SettleCdrGprsErrorDTO;
//import com.cmit.mvne.billing.settle.entity.dto.SettleCdrSmsErrorDTO;
//import com.cmit.mvne.billing.settle.service.ReprocessErrorCdrService;
//import com.cmit.mvne.billing.settle.service.SettleCdrGprsErrorService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 结算错单重处理
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2020/6/7
// */
//@Slf4j
//@RestController
//@RequestMapping("/settle")
//public class ReprocessErrorCdrController {
//
//    @Autowired
//    private ReprocessErrorCdrService reprocessErrorCdrService;
//
////    @Autowired
////    private SettleCdrGprsErrorService settleCdrGprsErrorService;
//
//
//    @GetMapping("/errorCdrs")
//    public ServerResponse getSettleErrorCdrs(@RequestParam(required = false) String filename,
//                                             @RequestParam String businessType,
//                                             @RequestParam String status,
//                                             @RequestParam(required = false) String msisdn,
//                                             @RequestParam(required = false) String errorCode,
//                                             @RequestParam(required = false) Long startTime,
//                                             @RequestParam(required = false) Long endTime,
//                                             @PageableDefault Pageable pageable) {
//        log.info("Invoking /settle/errorCdrs interface!");
//        if ("Data".equals(businessType)) {
//            List<SettleCdrGprsErrorDTO> settleGprsErrorCdrs = reprocessErrorCdrService.getSettleGprsErrorCdrs(filename, status, msisdn, errorCode, startTime, endTime, pageable);
//            return ServerResponse.createSuccess(settleGprsErrorCdrs);
//        } else if("SMS".equals(businessType)) {
//            List<SettleCdrSmsErrorDTO> settleSmsErrorCdrs = reprocessErrorCdrService.getSettleSmsErrorCdrs(filename, status, msisdn, errorCode, startTime, endTime, pageable);
//            return  ServerResponse.createSuccess(settleSmsErrorCdrs);
//        } else {
//            return ServerResponse.createErrorWithMessage("Cannot support BusinessType :" + businessType);
//        }
//    }
//
////    @GetMapping("/errorCdrs/excel")
////    public ServerResponse getSettleErrorCdrs(@RequestParam String businessType, @RequestBody List<Long> idList) {
////        log.info("Invoking /settle/errorCdrs interface!");
////        if ("Data".equals(businessType)) {
////            List<SettleCdrGprsErrorDTO> settleGprsErrorCdrs = ;
////            return ServerResponse.createSuccess(settleGprsErrorCdrs);
////        } else if("SMS".equals(businessType)) {
////            List<SettleCdrSmsErrorDTO> settleSmsErrorCdrs = reprocessErrorCdrService.getSettleSmsErrorCdrs(filename, status, msisdn, errorCode, startTime, endTime, pageable);
////            return  ServerResponse.createSuccess(settleSmsErrorCdrs);
////        } else {
////            return ServerResponse.createErrorWithMessage("Cannot support BusinessType :" + businessType);
////        }
////    }
//
//    @PostMapping("errorCdrs/gprs/reprocess")
//    public ServerResponse<String> reprocessSettleErrorCdrs(@RequestParam String businessType, @RequestBody List<Long> idList) {
//        log.info("Invoking /settle/errorCdrs/reprocess interface! param: {}", idList);
//
//        if ("Data".equals(businessType)) {
//            reprocessErrorCdrService.reprocessGprs(idList);
//            return ServerResponse.createSuccess();
//        } else if("SMS".equals(businessType)) {
////            reprocessErrorCdrService.reprocessSms(idList);
//            return ServerResponse.createErrorWithMessage("Cannot support BusinessType SMS reprocess!");
//        } else {
//            return ServerResponse.createErrorWithMessage("Cannot support BusinessType :" + businessType);
//        }
//
//    }
//
//
//
//
//
////    @GetMapping("/errorCdrs/excel")
////    public void downloadSettleErrorCdrs(@RequestParam(required = false) String filename,
////                                             @RequestParam String businessType,
////                                             @RequestParam String status,
////                                             @RequestParam(required = false) String msisdn,
////                                             @RequestParam(required = false) String errorCode,
////                                             @RequestParam(required = false) Long startTime,
////                                             @RequestParam(required = false) Long endTime,
////                                             @PageableDefault Pageable pageable, HttpServletResponse response) {
////        log.info("Invoking /settle/errorCdrs interface!");
////        if ("Data".equals(businessType)) {
////            List<SettleCdrGprsErrorDTO> settleGprsErrorCdrs = reprocessService.getSettleGprsErrorCdrs(filename, status, msisdn, errorCode, startTime, endTime, pageable);
////
////        } else if("SMS".equals(businessType)) {
////            List<SettleCdrSmsErrorDTO> settleSmsErrorCdrs = reprocessService.getSettleSmsErrorCdrs(filename, status, msisdn, errorCode, startTime, endTime, pageable);
////
////        } else {
////            return ServerResponse.createErrorWithMessage("Cannot support BusinessType :" + businessType);
////        }
////    }
//
//
//
////    @GetMapping("siminnSumDayAmount/excel")
////    public void downloadSettleSiminnSumDayAmountExcel(@RequestParam Long startTime, @RequestParam Long endTime, HttpServletResponse response) throws IOException {
////        log.info("Invoking /settle/siminnSumDayAmount/excel interface, params include startTime:{}, endTime:{}", startTime, endTime);
////        List<SiminnSumAmountDTO> siminnSumAmountDTOList = settleSiminnSumService.getSettleSiminnSumDay(startTime, endTime);
////
////        response.setContentType("application/vnd.ms-excel");
////        response.setCharacterEncoding("utf-8");
////        String filename = URLEncoder.encode(startTime.equals(startTime)?("siminn_sum_amount _"+ DateTimeUtil.getDateTimeofTimestamp(startTime).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
////                :("siminn_sum_amount_"+ DateTimeUtil.getDateTimeofTimestamp(startTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
////                + "_" + DateTimeUtil.getDateTimeofTimestamp(endTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"))), "UTF-8");
////        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
////
////        EasyExcel.write(response.getOutputStream(), SiminnSumAmountDTO.class).sheet("sheet1").doWrite(siminnSumAmountDTOList);
////    }
//
//
//}
