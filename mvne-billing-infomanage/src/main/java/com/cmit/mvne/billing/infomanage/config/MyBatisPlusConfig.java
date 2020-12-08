package com.cmit.mvne.billing.infomanage.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class MyBatisPlusConfig {
    public static ThreadLocal<String> tableNameHolder = new ThreadLocal<>();
    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();

        // 动态表名SQL解析器
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
        // Map的key就是需要替换的原始表名
        tableNameHandlerMap.put("rating_cdr_gprs", new ITableNameHandler(){
            @Override
            //public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
            public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
                // 自定义表名规则，或者从配置文件、request上下文中读取
                //String yearMonth = null;
                // 假设这里的用户表根据年份来进行分表操作
                //Date date = new Date();
                // 这里得到的是时间戳
/*                yearMonth = getParamValue("finishTime", metaObject);
                if (null == yearMonth) {
                    yearMonth = getPassValue(metaObject);
                }

                String yearMonth = getParam();*/

                // 返回最后需要操作的表名
                return "rating_cdr_gprs_" + tableNameHolder.get();
            }
        });
        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);
        sqlParserList.add(dynamicTableNameParser);
        paginationInterceptor.setSqlParserList(sqlParserList);

        return paginationInterceptor;


    }
}
