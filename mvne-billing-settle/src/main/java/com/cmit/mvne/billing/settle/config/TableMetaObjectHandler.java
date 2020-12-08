package com.cmit.mvne.billing.settle.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 字段填充，处理 @TableField(fill = FieldFill.INSERT) 一类注解
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/25
 */
@Slf4j
@Component
public class TableMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("start insert fill ....");
        this.setFieldValByName("createTime", new Date(), metaObject);
//        this.strictInsertFill(metaObject, "operator", String.class, "Jetty");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        this.setFieldValByName("updateTime", new Date(), metaObject);
//        this.strictUpdateFill(metaObject, "operator", String.class, "Tom");
    }

}
