package com.cmit.mvne.billing.settle.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 结算科目流量话单汇总对象
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
@Data
public class SettleItemGprsVolDTO {

    private String settleItem;
    private BigDecimal downloadVolSum;
    private BigDecimal uploadVolSum;

}
