/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.remote;

import org.springframework.stereotype.Component;

import com.cmit.mvne.billing.creditcontrol.entity.MvneCrmResponse;
import com.cmit.mvne.billing.creditcontrol.remote.entity.DelUserRequestDTO;
import com.cmit.mvne.billing.creditcontrol.remote.entity.StopAndStartDTO;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 远程调用CRM接口的服务降级类
 * @author jiangxm02
 *
 */
@Component
@Slf4j
public class MvneCrmClientFallbackFactory implements FallbackFactory<MvneCrmClient>{

	@Override
	public MvneCrmClient create(Throwable cause) {
		
		return new MvneCrmClient() {

			@Override
			public MvneCrmResponse stopAndStart(StopAndStartDTO stopAndStartDto) {
				String methodName = Thread.currentThread() .getStackTrace()[1].getMethodName();
				log.error("调用"+methodName+"方法时出现异常或超时，执行服务降级方法。",cause);
				return new MvneCrmResponse().fail().message("调用"+methodName+"方法时出现异常或超时，执行服务降级方法。");
			}

			@Override
			public MvneCrmResponse deleteUser(DelUserRequestDTO deleteUserDto) {
				String methodName = Thread.currentThread() .getStackTrace()[1].getMethodName();
				log.error("调用"+methodName+"方法时出现异常或超时，执行服务降级方法。",cause);
				return new MvneCrmResponse().fail().message("调用"+methodName+"方法时出现异常或超时，执行服务降级方法。");
			}
			
		};
	}

}
