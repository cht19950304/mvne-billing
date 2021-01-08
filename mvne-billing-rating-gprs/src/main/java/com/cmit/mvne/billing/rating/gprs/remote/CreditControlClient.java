package com.cmit.mvne.billing.rating.gprs.remote;

import com.cmit.mvne.billing.rating.gprs.creditcontrol.MvneCrmResponse;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.SmsGatewayDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "mvne-billing-credit-control", fallbackFactory = CreditControlClientFallBackFactory.class)
public interface CreditControlClient {
	
	@PostMapping(value = "/creditControl/sms")
	MvneCrmResponse sms(@RequestBody SmsGatewayDto smsGatewayDto);

	@PostMapping(value = "/creditControl/smsList")
	MvneCrmResponse smsList(@RequestBody List<SmsGatewayDto> smsGatewayDtoList);

}
