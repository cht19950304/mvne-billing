package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

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
@TableName("aps_balance_fee")
public class ApsBalanceFee  {

    /**
     * 用户ID
     */
    @TableId(value = "USER_ID", type = IdType.INPUT)
    @NotNull
    private Long userId;

    /**
     * 用户余额
     */
    @TableField("REMAIN_FEE")
    @NotNull
    private BigDecimal remainFee;

    /**
     * 单位
     */
    @TableField("MEASURE_ID")
    @NotNull
    private BigDecimal measureId;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    @NotNull
    private Date updateTime;

    @Version
    @TableField("VERSION")
    private Long version;

    public ApsBalanceFee(Long userId,BigDecimal remainFee,BigDecimal measureId,Date updateTime){
        this.userId = userId;
        this.remainFee = remainFee;
        this.measureId = measureId;
        this.updateTime = updateTime;
        this.version = 1L;
    }

    public ApsBalanceFee(Map<Object, Object> balanceFeeMap) throws ParseException {
        Class<BigDecimal> bigDecimalClass = BigDecimal.class;
        Class<Long> longClass = Long.class;
        Class<Date> dateClass = Date.class;

        ObjectMapper objectMapper = new ObjectMapper();
        this.measureId = objectMapper.convertValue(balanceFeeMap.get("measureId"), bigDecimalClass);
        this.remainFee = objectMapper.convertValue(balanceFeeMap.get("remainFee"), bigDecimalClass);
        this.updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(balanceFeeMap.get("updateTime").toString());
        this.userId = objectMapper.convertValue(balanceFeeMap.get("userId"), longClass);
    }

    public String toJsonString()
    {
        return JSON.toJSONString(this);
    }
}
