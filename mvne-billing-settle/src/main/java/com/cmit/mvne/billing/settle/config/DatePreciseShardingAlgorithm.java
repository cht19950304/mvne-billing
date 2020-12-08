//package com.cmit.mvne.billing.settle.config;
//
//import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
//import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.Collection;
//import java.util.Date;
//
///**
// * 准确时间分片算法
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2020/3/6
// */
//public class DatePreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {
//
////    private DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyyMM");
//    @Override
//    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
//        StringBuilder tableName = new StringBuilder();
//        String logicTableName = shardingValue.getLogicTableName();
////        shardingValue.getValue()
////        Instant instant = shardingValue.getValue().toInstant();
////        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
////        String yearMonth = localDateTime.format(dateformat);
//        String yyyyMM = shardingValue.getValue();
//        tableName.append(logicTableName).append("_").append(yyyyMM);
//        return tableName.toString();
//    }
//}
//
////
////@Service
////public class DatePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {
////
////    private DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyyMM");
////    @Override
////    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
////        StringBuilder tableName = new StringBuilder();
////        String logicTableName = shardingValue.getLogicTableName();
////        Instant instant = shardingValue.getValue().toInstant();
////        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
////        String yearMonth = localDateTime.format(dateformat);
////
////        tableName.append(logicTableName).append("_").append(yearMonth);
////        return tableName.toString();
////    }
////}
