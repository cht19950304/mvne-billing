/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.remote;

import com.cmit.mvne.billing.creditcontrol.entity.MvneCrmResponse;
import com.cmit.mvne.billing.creditcontrol.remote.entity.DelUserRequestDTO;
import com.cmit.mvne.billing.creditcontrol.remote.entity.StopAndStartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description 远程调用CRM的接口，在信控这里就是根据条件调用停复机和销户接口
 * @author jiangxm02
 *
 */
@FeignClient(value="MVNE-CRM-CORE",fallbackFactory=MvneCrmClientFallbackFactory.class)
public interface MvneCrmClient {
	
	@PostMapping(value="/creditControl/stopAndStart")
	MvneCrmResponse stopAndStart(@RequestBody StopAndStartDTO stopAndStartDto);
	
	@PostMapping(value="/creditControl/deleteUser")
	MvneCrmResponse deleteUser(@RequestBody DelUserRequestDTO deleteUserDto);
}
