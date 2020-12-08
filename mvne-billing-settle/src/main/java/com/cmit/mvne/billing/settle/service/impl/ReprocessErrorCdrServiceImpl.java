//package com.cmit.mvne.billing.settle.service.impl;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.cmit.mvne.billing.settle.common.ReprocessStatus;
//import com.cmit.mvne.billing.settle.common.SettleErrorCode;
//import com.cmit.mvne.billing.settle.entity.SettleCdrGprs;
//import com.cmit.mvne.billing.settle.entity.SettleCdrGprsError;
//import com.cmit.mvne.billing.settle.entity.dto.SettleCdrGprsErrorDTO;
//import com.cmit.mvne.billing.settle.entity.dto.SettleCdrSmsErrorDTO;
//import com.cmit.mvne.billing.settle.service.*;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2020/6/8
// */
//@Service
//public class ReprocessErrorCdrServiceImpl implements ReprocessErrorCdrService {
//
//    @Autowired
//    private SettleCdrGprsErrorService settleCdrGprsErrorService;
//    @Autowired
//    private SettleCdrSmsErrorService settleCdrSmsErrorService;
//
//    @Autowired
//    private GprsSettleService gprsSettleService;
//    @Autowired
//    private SettleCdrGprsService settleCdrGprsService;
//
//    @Override
//    public List<SettleCdrGprsErrorDTO> getSettleGprsErrorCdrs(String filename, String status, String msisdn, String errorCode, Long startTime, Long endTime, Pageable pageable) {
//        Page<SettleCdrGprsErrorDTO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize(), true);
//        List<SettleCdrGprsErrorDTO> settleCdrGprsErrorDTOList = settleCdrGprsErrorService.selectSettleCdrGprsErrorByCondition(page, filename, status, msisdn, errorCode, startTime, endTime);
//        return settleCdrGprsErrorDTOList;
//    }
//
//    @Override
//    public List<SettleCdrSmsErrorDTO> getSettleSmsErrorCdrs(String filename, String status, String msisdn, String errorCode, Long startTime, Long endTime, Pageable pageable) {
//        Page<SettleCdrSmsErrorDTO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize(), true);
//        List<SettleCdrSmsErrorDTO> settleCdrSmsErrorDTOList = settleCdrSmsErrorService.selectSettleCdrSmsErrorByCondition(page, filename, status, msisdn, errorCode, startTime, endTime);
//        return settleCdrSmsErrorDTOList;
//    }
//
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void reprocessGprs(List<Long> idList) {
//        Collection<SettleCdrGprsError> settleCdrGprsErrorList = settleCdrGprsErrorService.listByIds(idList);
//
//        List<SettleCdrGprs> settleCdrGprsList = new ArrayList<>();
//        List<SettleCdrGprsError> processedErrorList = new ArrayList<>();
//        for(SettleCdrGprsError settleCdrGprsError: settleCdrGprsErrorList) {
//
//            SettleErrorCode verifyResult = gprsSettleService.verify(settleCdrGprsError.getMsisdn());
//            if (verifyResult != null) {
//                settleCdrGprsError.setStatus(ReprocessStatus.FAIL);
//                settleCdrGprsError.setErrorCode(verifyResult.getErrorCode());
//                settleCdrGprsError.setErrorMessage(verifyResult.getErrorMessage());
//                settleCdrGprsError.setRedoTime(new Date());
//
//                processedErrorList.add(settleCdrGprsError);
//
//            } else {
//                SettleCdrGprs settleCdrGprs = new SettleCdrGprs();
//                BeanUtils.copyProperties(settleCdrGprsError, settleCdrGprs);
//
//                settleCdrGprsError.setStatus(ReprocessStatus.SUCCESS);
//                settleCdrGprsError.setRedoTime(new Date());
//
//                settleCdrGprsList.add(settleCdrGprs);
//                processedErrorList.add(settleCdrGprsError);
//            }
//
//            settleCdrGprsService.saveBatch(settleCdrGprsList);
//            settleCdrGprsErrorService.saveOrUpdateBatch(processedErrorList);
//        }
//    }
//
//}
