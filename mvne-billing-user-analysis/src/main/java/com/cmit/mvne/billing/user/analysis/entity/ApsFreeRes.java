package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@Slf4j
@TableName("aps_free_res")
public class ApsFreeRes  {


    /**
     * 用户ID
     */
    @TableId(value = "USER_ID", type = IdType.INPUT)
    @NotNull
    private Long userId;

    @TableField("PRODUCT_INS_ID")
    @NotNull
    private Long productInsId;

    /**
     * 免费资源科目
     */
    @TableField("ITEM_ID")
    @NotNull
    private Long itemId;

    /**
     * 已用资源量
     */
    @TableField("USED_VALUE")
    @NotNull
    private BigDecimal usedValue;

    /**
     * 免费资源总量
     */
    @TableField("AMOUNT")
    @NotNull
    private BigDecimal amount;

    /**
     * 单位
     */
    @TableField("MEASURE_ID")
    @NotNull
    private BigDecimal measureId;

    /**
     * 生效时间
     */
    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    @NotNull
    private Date validDate;

    /**
     * 失效时间
     */
    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    @NotNull
    private Date expireDate;

    @Version
    @TableField("VERSION")
    private Long version;

    public ApsFreeRes(FreeRes freeRes,Long productInsId, Long userId,Date validDate,Date expireDate){
        this.userId = userId;
        this.productInsId = productInsId;
        this.itemId = freeRes.getFreeresItem();
        this.usedValue = new BigDecimal(0) ;
        this.amount = freeRes.getAmount();
        this.measureId = freeRes.getMeasureId();
        this.validDate = validDate;
        this.expireDate = expireDate;
        this.version = 1L;

    }

    public ApsFreeRes(Map<Object, Object> freeResMap) throws ParseException {
        Class<BigDecimal> bigDecimalClass = BigDecimal.class;
        Class<Long> longClass = Long.class;
        Class<Date> dateClass = Date.class;

        ObjectMapper objectMapper = new ObjectMapper();
        this.amount = objectMapper.convertValue(freeResMap.get("amount"), bigDecimalClass);
        this.expireDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(freeResMap.get("expireDate").toString());
        this.itemId = objectMapper.convertValue(freeResMap.get("itemId"), longClass);
        this.productInsId = objectMapper.convertValue(freeResMap.get("productInsId"), longClass);
        this.measureId = objectMapper.convertValue(freeResMap.get("measureId"), bigDecimalClass);
        this.userId = objectMapper.convertValue(freeResMap.get("userId"), longClass);
        this.usedValue = objectMapper.convertValue(freeResMap.get("usedValue"), bigDecimalClass);
        this.validDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(freeResMap.get("validDate").toString());
    }

    public ApsFreeRes(String freeRes) {
        JSONObject jsonObject = JSON.parseObject(freeRes);
        this.amount = jsonObject.getBigDecimal("amount");
        this.expireDate = jsonObject.getDate("expireDate");
        this.itemId = jsonObject.getLong("itemId");
        this.measureId = jsonObject.getBigDecimal("measureId");
        this.userId = jsonObject.getLong("userId");
        this.usedValue = jsonObject.getBigDecimal("usedValue");
        this.validDate = jsonObject.getDate("validDate");
    }

    public String toJsonString()
    {
        return JSON.toJSONString(this);
    }


}
