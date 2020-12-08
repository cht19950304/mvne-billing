package com.cmit.mvne.billing.settle.config;

import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/12
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 动态存储动态表名
     */
    public static ThreadLocal<String> tableNameHolder=new ThreadLocal<>();

    /**
     * 动态表名SQl解析器
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        dynamicTableNameParser.setTableNameHandlerMap(new HashMap<String, ITableNameHandler>(2) {{
            put("settle_cdr_gprs", (metaObject, sql, tableName) -> {
                // metaObject 可以获取传入参数，这里实现你自己的动态规则
//                String year = "_2018";
//                int random = new Random().nextInt(10);
//
//                if (random % 2 == 1) {
//                    year = "_2019";
//                }
//                Object receiveTime = getParamValue("receiveTime", metaObject);
//                String tableSuffix = getTableSuffixByReceiveTime(receiveTime);
//                System.out.println(getParamValue("receiveTime", metaObject));
//                return tableName + "_" + tableSuffix;
                // 返回null不替换
                return tableNameHolder.get();
            });
        }});
        paginationInterceptor.setSqlParserList(Collections.singletonList(dynamicTableNameParser));
        return paginationInterceptor;
    }

//    private Object getParamValue(String title, MetaObject metaObject){
//        //获取参数
//        Object originalObject = metaObject.getOriginalObject();
//
//        JSONObject originalObjectJSON = JSON.parseObject(JSON.toJSONString(originalObject));
//        JSONObject boundSql = originalObjectJSON.getJSONObject("boundSql");
//        JSONObject parameterObject = boundSql.getJSONObject("parameterObject");
//        return parameterObject.get(title);
//    }

//    /**
//     * 根据系统时区获取表尾
//     * @param receiveTime
//     * @return
//     */
//    private String getTableSuffixByReceiveTime(Object receiveTime) {
//        Instant instant = Instant.ofEpochMilli((Long) receiveTime);
//        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//    }

}
