package com.cmit.mvne.billing.infomanage.remote.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
/**
 * @Description 用于封装调用信控的参数
 * @author luxf
 *
 */
@Data
public class SmsGatewayDto {
    @NotBlank
    private String msisdn;
    @NotBlank
    private String text;
    @NotBlank
    private String operation;
    @NotBlank
    private String reason;

}
