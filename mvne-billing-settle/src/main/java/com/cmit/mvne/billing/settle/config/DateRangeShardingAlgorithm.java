//package com.cmit.mvne.billing.settle.config;
//
//import com.google.common.collect.Range;
//import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
//import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;
//
//import java.time.LocalDate;
//import java.time.Period;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
///**
// * 区域时间分片算法
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2020/3/6
// */
//
//public class DateRangeShardingAlgorithm implements RangeShardingAlgorithm<String> {
//
//    private  DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyyMM");
//    @Override
//    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {
//        Collection<String> result = new LinkedHashSet<String>();
//        Range<Date> shardingKey = shardingValue.getValueRange();
//        LocalDate startDateTime = shardingKey.lowerEndpoint().toInstant()
//                .atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate endDateTime = shardingKey.upperEndpoint().toInstant()
//                .atZone(ZoneId.systemDefault()).toLocalDate();
//
//        //获取跨月份的表 如202001,202002
//        List<String> yearMonthStringList = getYearMonthBetween(startDateTime, endDateTime);
//
//        for(String yearMonth : yearMonthStringList) {
//            StringBuilder tableName = new StringBuilder();
//            tableName.append(shardingValue.getLogicTableName())
//                    .append("_").append(yearMonth);
//            result.add(tableName.toString());
//        }
//        return result;
//    }
//
//    private List<String> getYearMonthBetween(LocalDate startDate, LocalDate endDate) {
//        // 2020-01 与 2020-03 计算得值为1
//        int months = Period.between(startDate, endDate).getMonths();
//        List<String> yearMonthList = new ArrayList<>();
//        yearMonthList.add(startDate.format(dateformat));
//        for(int i=0; i<months; i++) {
//            LocalDate localDate = startDate.plusMonths(1);
//            yearMonthList.add(localDate.format(dateformat));
//        }
//        if(months>0) {
//            yearMonthList.add(endDate.format(dateformat));
//        }
//        return yearMonthList;
//    }
//}
//
////public class DateRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {
////
////    private  DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyyMM");
////    @Override
////    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {
////        Collection<String> result = new LinkedHashSet<String>();
////        Range<Date> shardingKey = shardingValue.getValueRange();
////        LocalDate startDateTime = shardingKey.lowerEndpoint().toInstant()
////                .atZone(ZoneId.systemDefault()).toLocalDate();
////        LocalDate endDateTime = shardingKey.upperEndpoint().toInstant()
////                .atZone(ZoneId.systemDefault()).toLocalDate();
////
////        //获取跨月份的表 如202001,202002
////        List<String> yearMonthStringList = getYearMonthBetween(startDateTime, endDateTime);
////
////        for(String yearMonth : yearMonthStringList) {
////            StringBuilder tableName = new StringBuilder();
////            tableName.append(shardingValue.getLogicTableName())
////                    .append("_").append(yearMonth);
////            result.add(tableName.toString());
////        }
////        return result;
////    }
////
////    private List<String> getYearMonthBetween(LocalDate startDate, LocalDate endDate) {
////        // 2020-01 与 2020-03 计算得值为1
////        int months = Period.between(startDate, endDate).getMonths();
////        List<String> yearMonthList = new ArrayList<>();
////        yearMonthList.add(startDate.format(dateformat));
////        for(int i=0; i<months; i++) {
////            LocalDate localDate = startDate.plusMonths(1);
////            yearMonthList.add(localDate.format(dateformat));
////        }
////        if(months>0) {
////            yearMonthList.add(endDate.format(dateformat));
////        }
////        return yearMonthList;
////    }
////}
