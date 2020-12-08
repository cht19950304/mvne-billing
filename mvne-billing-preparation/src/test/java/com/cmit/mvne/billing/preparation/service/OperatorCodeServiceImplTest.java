//package com.cmit.mvne.billing.preparation.service;
//
//import com.cmit.mvne.billing.preparation.entity.BdOperatorCode;
//import com.cmit.mvne.billing.preparation.service.impl.OperatorCodeServiceImpl;
//import junit.framework.TestCase;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
///**
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2020/4/16
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Slf4j
//public class OperatorCodeServiceImplTest {
//
//    @Autowired
//    private OperatorCodeServiceImpl operatorCodeServiceImpl;
//
//    @Test
//    public void findByoperatorCode() {
//
//        List<BdOperatorCode> abwdc = operatorCodeServiceImpl.findByOperatorCode("ABWDC");
//
////        TestCase.assertNull(abwdc);
//        TestCase.assertEquals("ABW", abwdc.get(0).getCountryInitials());
//    }
//
//    @Test
//    public void updateOperatorCode() throws InterruptedException {
////        BdOperatorCode operatorCode = new BdOperatorCode();
////        operatorCode.setId(1L);
////        operatorCode.setCountryInitials("ABW");
////        operatorCode.setCountryName("Aruba");
////        operatorCode.setCountryNameCn("阿鲁巴岛");
////        operatorCode.setOperatorCode("ABWDC");
////        operatorCode.setOperatorName("New Millennium Telecom Services NV2");
//        List<BdOperatorCode> abwdcList = operatorCodeServiceImpl.updateByOperatorCode("ABWDC", "111");
//
//        TestCase.assertEquals("111", abwdcList.get(0).getOperatorName());
//        Thread.currentThread().sleep(5000);
//    }
//}