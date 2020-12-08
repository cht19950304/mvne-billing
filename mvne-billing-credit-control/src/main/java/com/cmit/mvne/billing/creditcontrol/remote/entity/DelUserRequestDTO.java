package com.cmit.mvne.billing.creditcontrol.remote.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 销户请求参数
 *
 * @author jiangxm
 * @date 2020/02/17 9:18
 */
@Data
public class DelUserRequestDTO {
    /**
     * 手机号
     */
    @NotNull
    private String billId;
    /**
     * 订单来源类型
     */
    @NotNull
    private String fromType;
    /**
     * 基础运营商Id
     */
    @NotNull
    private String basicOrgId;
    /**
     * 转售商Id
     */
    @NotNull
    private String mvnoId;
}
