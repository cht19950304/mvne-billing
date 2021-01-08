package com.cmit.mvne.billing.rating.gprs.remote;

import com.cmit.mvne.billing.rating.gprs.creditcontrol.MvneCrmResponse;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.SmsGatewayDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CreditControlClientFallBackFactory implements FallbackFactory<CreditControlClient> {

	@Override
	public CreditControlClient create(Throwable e) {
		return new CreditControlClient() {
			@Override
			public MvneCrmResponse sms(SmsGatewayDto smsGatewayDto) {
				String methodName = Thread.currentThread() .getStackTrace()[1].getMethodName();
				log.error("Called " + methodName + " error，called fallback credit control!", e.getMessage(), e);
				return new MvneCrmResponse().fail().message("Called credit control failed!");
			}

			@Override
			public MvneCrmResponse smsList(List<SmsGatewayDto> smsGatewayDtoList) {
				String methodName = Thread.currentThread() .getStackTrace()[1].getMethodName();
				log.error("Called " + methodName + " error，called fallback credit control!", e.getMessage(), e);
				return new MvneCrmResponse().fail().message("Called credit control failed!");
			}
		};
	}
}
