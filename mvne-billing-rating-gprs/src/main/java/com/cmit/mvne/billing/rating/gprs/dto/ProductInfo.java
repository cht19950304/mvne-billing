package com.cmit.mvne.billing.rating.gprs.dto;

import com.cmit.mvne.billing.rating.gprs.remote.Resource;
import com.cmit.mvne.billing.rating.gprs.util.MeasureExchangeUtils;
import com.cmit.mvne.billing.user.analysis.entity.FreeRes;
import com.cmit.mvne.billing.user.analysis.entity.PmProduct;
import com.cmit.mvne.billing.user.analysis.entity.RatingRate;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/26 15:38
 */
@Data
public class ProductInfo {

    /**
     * 销售品ID
     */
    private Long productId;

    /**
     * 产品资费，单位：欧元
     */
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal productFee;

    /**
     * 免费资源，单位默认为MB
     */
    @NotEmpty
    private List<Resource> resource = new ArrayList<>();

    /**
     * 套外资费，欧元/MB
     */
    @NotEmpty
    private List<Resource> overFee = new ArrayList<>();

    /**
     * 适用区域
     */
    @NotNull
    @NotBlank
    private String regionCode;

    /**
     * group id
     */
    private Integer groupId;

    /**
     * 产品名称
     */
    @NotBlank
    @NotEmpty
    @NotNull
    private String productName;

    /**
     * 已生效产品（PRODUCT_STATUS 为4）
     * 已下架产品（PRODUCT_STATUS 为6）
     * 未生效产品（PRODUCT_STATUS 为1）
     */
    private int productStatus;

    /**
     * 产品的有效期（单位：月）
     * 最小为1
     */
    @NotNull
    @Min(1)
    private Integer effectiveTime;

    /**
     * 产品生效时间
     */
    @NotNull
    private Long effectiveDate;

    /**
     * 产品失效时间
     */
    @NotNull
    private Long expireDate;

    /**
     * 描述
     */
    private String desc;

    public ProductInfo(Date now, PmProduct pmProduct, @NotNull FreeRes freeRes, @NotNull RatingRate ratingRate, BigDecimal originResourceMeasure) {
        this.productId = pmProduct.getProductOfferingId();
        this.productName = pmProduct.getName();
        this.productFee = pmProduct.getFee();

        BigDecimal measureMB = new BigDecimal(105);
        // 查询产品的免费资源，必然整数
        // 转成创建产品时填的单位
        BigDecimal destValue = MeasureExchangeUtils.exchangeFloor(freeRes.getAmount(), freeRes.getMeasureId(), originResourceMeasure, 2);
        this.resource.add(new Resource(destValue, originResourceMeasure));

        BigDecimal measureEuro = new BigDecimal(10302);
        BigDecimal feeAmount = ratingRate.getRateVal();
        BigDecimal feeMeasure = ratingRate.getMeasureMoney();
        // 转成欧元，必然两位小数，unnecessary
        BigDecimal amountEuro = MeasureExchangeUtils.exchangeFloor(feeAmount, feeMeasure, measureEuro, 2);

        BigDecimal valueAmount = ratingRate.getCycleUnit();
        BigDecimal valueMeasure = ratingRate.getMeasureId();
        // 查询产品套外，转成MB，必然整数
        BigDecimal valueMB = MeasureExchangeUtils.exchangeFloor(valueAmount, valueMeasure, measureMB, 2);
        this.overFee.add(new Resource(amountEuro, measureEuro, valueMB, measureMB));

        this.regionCode = pmProduct.getZoneGroup();
        this.effectiveTime = pmProduct.getEffectiveTime();
        this.effectiveDate = pmProduct.getEffectiveDate().getTime();
        this.expireDate = pmProduct.getExpireDate().getTime();
        this.productStatus = getStatus(now, pmProduct.getEffectiveDate(), pmProduct.getExpireDate());
        this.groupId = pmProduct.getGroupId();
        this.desc = pmProduct.getProdDesc();
    }

    public ProductInfo(String desc) {
        this.desc = desc;
    }

    private int getStatus(Date now, Date effectiveDate, Date expireDate) {
        if ((now.compareTo(effectiveDate)>0) && (now.compareTo(expireDate)<0)) {
            return 4;
        } else if (now.compareTo(effectiveDate)<0) {
            return 1;
        } else {
            //(now.compareTo(expireDate)>0)
            return 6;
        }

    }
}
