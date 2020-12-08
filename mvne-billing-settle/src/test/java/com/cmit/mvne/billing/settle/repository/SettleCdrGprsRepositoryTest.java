//package com.cmit.mvne.billing.settle.repository;
//
//import com.cmit.mvne.billing.settle.entity.SettleCdrGprs;
//import junit.framework.TestCase;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Optional;
//
///**
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2020/3/7
// */
//@Slf4j
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class SettleCdrGprsRepositoryTest {
//
//    @Autowired
//    SettleCdrGprsRepository settleCdrGprsRepository;
//
//    @Test
//    public void testFindAllById() {
//
//        Optional<SettleCdrGprs> byId = settleCdrGprsRepository.findById("c0a80107-70ad-1c24-8170-adcc81ef0000");
//
//        TestCase.assertTrue(byId.isPresent());
//
//    }
//
//    @Test
//    public void testFindAllByLocalEventTimestampBetween() throws ParseException {
//        List<SettleCdrGprs> allByLocalEventTimestampBetween = settleCdrGprsRepository.findAllByLocalEventTimestampBetween(
//                new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2020-01-01 00:00"),
//                new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2020-01-31 23:59"));
//        TestCase.assertTrue(allByLocalEventTimestampBetween.size()>0);
//    }
//
//}