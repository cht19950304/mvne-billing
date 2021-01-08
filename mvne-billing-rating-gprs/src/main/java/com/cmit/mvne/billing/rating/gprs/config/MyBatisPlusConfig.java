package com.cmit.mvne.billing.rating.gprs.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/3/6 14:25
 */
@Configuration
public class MyBatisPlusConfig {
    @Value(value = "${rating.load.format}")
    String tableFormat;

    private String param = "ss";

    public static ThreadLocal<String> tableNameHolder = new ThreadLocal<>();

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return this.param;
    }

    // 拦截执行的sql
    /*@Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //sql格式化
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }*/

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(-1);

        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();

        // 动态表名SQL解析器
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
        // Map的key就是需要替换的原始表名
        tableNameHandlerMap.put("rating_cdr_gprs", new ITableNameHandler(){
            @Override
            public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
                // 自定义表名规则，或者从配置文件、request上下文中读取
                /*String yearMonth = null;
                // 假设这里的用户表根据年份来进行分表操作
                //Date date = new Date();
                // 这里得到的是时间戳
                yearMonth = getParamValue("finishTime", metaObject);
                if (null == yearMonth) {
                    yearMonth = getPassValue(metaObject);
                }*/

                /*String yearMonth = getParam();*/

                // 返回最后需要操作的表名
                return "rating_cdr_gprs_" + tableNameHolder.get();
            }
        });
        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);
        sqlParserList.add(dynamicTableNameParser);
        paginationInterceptor.setSqlParserList(sqlParserList);


        return paginationInterceptor;
    }

    private String getParamValue(String title, MetaObject metaObject){
        //获取参数
        // 注意这种方法只适合于参数带有对象的方法
        Object originalObject = metaObject.getOriginalObject();
        JSONObject originalObjectJSON = JSON.parseObject(JSON.toJSONString(originalObject));
        JSONObject boundSql = originalObjectJSON.getJSONObject("boundSql");
        JSONObject parameterObject = boundSql.getJSONObject("parameterObject");
        String finishTime = String.valueOf(parameterObject.get(title));
        if (!"null".equals(finishTime)) {
            Long dateTime = Long.valueOf(finishTime);
            Date date = new Date(dateTime);
            String yearMonth = new SimpleDateFormat(tableFormat).format(date);
            return yearMonth;
        } else {
            return null;
        }
    }

    private String getPassValue(MetaObject metaObject){
        Object originalObject = metaObject.getValue("delegate.boundSql.parameterObject.ew.lastSql");
        String lastSql = String.valueOf(originalObject);
        String[] dateStr = lastSql.split(" ");
        return dateStr[2];
    }
}
