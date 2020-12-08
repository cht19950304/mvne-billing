package com.cmit.mvne.billing.infomanage.remote;

import com.cmit.mvne.billing.infomanage.common.MvneInfoManageResponse;
import com.cmit.mvne.billing.infomanage.remote.entity.SmsGatewayDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditControlClientFallbackFactory implements FallbackFactory<CreditControlClient> {


    @Override
    public CreditControlClient create(Throwable throwable) {
        return new CreditControlClient() {
            @Override
            public MvneInfoManageResponse userStart(SmsGatewayDto smsGatewayDto) {
                String methodName = Thread.currentThread() .getStackTrace()[1].getMethodName();
                log.error("调用"+methodName+"方法时出现异常或超时，执行服务降级方法。",throwable);
                return new MvneInfoManageResponse().fail().message("调用"+methodName+"方法时出现异常或超时，执行服务降级方法。");
            }
        };
    }
}
