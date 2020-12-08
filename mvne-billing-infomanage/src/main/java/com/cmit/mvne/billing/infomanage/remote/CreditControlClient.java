package com.cmit.mvne.billing.infomanage.remote;

import com.cmit.mvne.billing.infomanage.common.MvneInfoManageResponse;
import com.cmit.mvne.billing.infomanage.remote.entity.SmsGatewayDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Description 远程调用信控的接口，触发复机
 * @author luxf
 *
 */
@FeignClient(value = "MVNE-BILLING-CREDIT-CONTROL",fallbackFactory = CreditControlClientFallbackFactory.class)
//@FeignClient(value = "MVNE-BILLING-CREDIT-CONTROL",url = "http://296514k3b4.zicp.vip",fallbackFactory = CreditControlClientFallbackFactory.class)
public interface CreditControlClient {
    @PostMapping("/creditControl/sms")
    public MvneInfoManageResponse userStart(@RequestBody  SmsGatewayDto smsGatewayDto);
}

