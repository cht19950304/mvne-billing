//package com.cmit.mvne.billing.settle.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.hibernate.EmptyInterceptor;
//import org.hibernate.resource.jdbc.spi.StatementInspector;
//
///**
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2020/5/7
// */
//@Slf4j
//public class AutoTableNameInspector implements StatementInspector {
////    private String srcName = StringUtils.EMPTY; //源表名
////    private String destName = StringUtils.EMPTY; // 目标表名
////
////    public AutoTableNameInspector() {}
////
////    public AutoTableNameInspector(String srcName, String destName){
////        this.srcName = srcName;
////        this.destName = destName;
////    }
////    @Override
////    public String onPrepareStatement(String sql) {
////        if(srcName.equals(StringUtils.EMPTY) || destName.equals(StringUtils.EMPTY)){
////            return sql;
////        }
////        sql = sql.replaceAll(srcName, destName);
////        return sql;
////    }
//
//    @Override
//    public String inspect(String sql) {
//
//        log.info("hello");
//
//        return sql;
//    }
//}
