/**
 * 
 */
package com.cmit.mvne.billing.infomanage.controller;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.cmit.mvne.billing.infomanage.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.common.MvneInfoManageResponse;
import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IProdDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IUserDto;
import com.cmit.mvne.billing.infomanage.crm.service.InfoManageCrmService;
import com.cmit.mvne.billing.infomanage.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import com.cmit.mvne.billing.infomanage.crm.service.ChangeDateService;
/**
 *  用于接收来自CRM侧的请求，将信管流程的相关Order/User/Prod信息写入中间表
 * @author jiangxm02
 * @date 2019-12-16 16:13:06
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/infoManage")
public class InfoManageController {
	
	@Autowired
	private InfoManageCrmService infoManageCrmServiceImpl;
	@Autowired
	private ChangeDateService changeDateService;

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public void test(@RequestBody InfoManageCrmEntity infoManageCrmEntity) {
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&");
	}

	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public MvneInfoManageResponse createUser(@RequestBody @NotNull InfoManageCrmEntity infoManageCrmEntity) {
		try {
//			if(null == infoManageCrmEntity) {
//				log.error("Create user in info manage's necessary parameter is null");
//				return new MvneInfoManageResponse().fail().message("Create user in info manage's necessary parameter is null");
//			}
			IOrdOrderDto iOrdOrderDto = infoManageCrmEntity.getIordOrder();
			IProdDto iProdDto = infoManageCrmEntity.getIprod();
			IUserDto iUserDto = infoManageCrmEntity.getIuser();

			if(null != iOrdOrderDto && null != iProdDto && null != iUserDto) {
				List<Object> classList = changeDateService.createUserChange(iOrdOrderDto, iProdDto, iUserDto);
				System.out.println("Hello"+infoManageCrmEntity.getIordOrder().toString());
				//infoManageCrmServiceImpl.createUser(iOrdOrder, iProd, iUser);
				infoManageCrmServiceImpl.createUser((IOrdOrder) classList.get(0), (IProd) classList.get(1), (IUser) classList.get(2));
			}
			else {
				log.error("Create user in info manage'parameter like iOrdOrder or iProd or iUser is null");
				return new MvneInfoManageResponse().fail().message("Create user in info manage'parameter like iOrdOrder or iProd or iUser is null");
			}
			
		} catch (MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
		}
		return new MvneInfoManageResponse().success();
	}
	
	@PostMapping("changeOffer")
	@ResponseBody
	public MvneInfoManageResponse changeOffer(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		try {
//			if(null == infoManage) {
//				log.error("Change offer in info manage's necessary parameter is null");
//				return new MvneInfoManageResponse().fail().message("Change offer in info manage's necessary parameter is null");
//			}
			IOrdOrderDto iOrdOrderDto = infoManage.getIordOrder();
			IProdDto iProdDto = infoManage.getIprod();
			if(null != iOrdOrderDto && null != iProdDto) {
				List<Object> classList = changeDateService.changeOfferChange(iOrdOrderDto, iProdDto);
				infoManageCrmServiceImpl.changeOffer((IOrdOrder) classList.get(0), (IProd) classList.get(1));
			}
			else {
				log.error("Change offer in info manage'parameter like iOrdOrder or iProd is null");
				return new MvneInfoManageResponse().fail().message("Change offer in info manage'parameter like iOrdOrder or iProd is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
		}
		return new MvneInfoManageResponse().success();
		
	}
	
	@PostMapping("stop")
	@ResponseBody
	public MvneInfoManageResponse stop(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		try {
//			if(null == infoManage) {
//				log.error("Stop or start user in info manage's necessary parameter is null");
//				return new MvneInfoManageResponse().fail().message("Stop or start user in info manage's necessary parameter is null");
//			}
			IOrdOrderDto iOrdOrderDto = infoManage.getIordOrder();
			IUserDto iUserDto = infoManage.getIuser();
			if(null != iOrdOrderDto && null != iUserDto) {
				List<Object> classList = changeDateService.stopChange(iOrdOrderDto,iUserDto);
				infoManageCrmServiceImpl.stop((IOrdOrder) classList.get(0), (IUser) classList.get(1));
			}
			else {
				log.error("Stop user in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("Stop user in info manage'parameter like iOrdOrder or iUser is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
		}
		return new MvneInfoManageResponse().success();
	}

	@PostMapping("start")
	@ResponseBody
	public MvneInfoManageResponse start(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		try {
//			if(null == infoManage) {
//				log.error("Stop or start user in info manage's necessary parameter is null");
//				return new MvneInfoManageResponse().fail().message("Stop or start user in info manage's necessary parameter is null");
//			}
			IOrdOrderDto iOrdOrderDto = infoManage.getIordOrder();
			IUserDto iUserDto = infoManage.getIuser();
			if(null != iOrdOrderDto && null != iUserDto) {
				List<Object> classList = changeDateService.startChange(iOrdOrderDto,iUserDto);
				infoManageCrmServiceImpl.start((IOrdOrder) classList.get(0), (IUser) classList.get(1));
			}
			else {
				log.error("start user in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("start user in info manage'parameter like iOrdOrder or iUser is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
		}
		return new MvneInfoManageResponse().success();
	}
	
	@PostMapping("reCharge")
	@ResponseBody
	public MvneInfoManageResponse reCharge(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		try {
//			if(null == infoManage) {
//				log.error("Recharge in info manage's necessary parameter is null");
//				return new MvneInfoManageResponse().fail().message("Recharge in info manage's necessary parameter is null");
//			}
			IOrdOrderDto iOrdOrderDto = infoManage.getIordOrder();
			if(null != iOrdOrderDto) {
				List<Object> classList = changeDateService.reChargeChange(iOrdOrderDto);
				infoManageCrmServiceImpl.recharge((IOrdOrder) classList.get(0));
			}
			else {
				log.error("Recharge in info manage'parameter like iOrdOrder is null");
				return new MvneInfoManageResponse().fail().message("Recharge in info manage'parameter like iOrdOrder is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
		}
		return new MvneInfoManageResponse().success();
	}
	
	@PostMapping("deleteUser")
	@ResponseBody
	public MvneInfoManageResponse deleteUser(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		try {
//			if(null == infoManage) {
//				log.error("Delete user in info manage's necessary parameter is null");
//				return new MvneInfoManageResponse().fail().message("Delete user in info manage's necessary parameter is null");
//			}
			IOrdOrderDto iOrdOrderDto = infoManage.getIordOrder();
			IUserDto iUserDto = infoManage.getIuser();
			if(null != iOrdOrderDto && null != iUserDto) {
				List<Object> classList = changeDateService.deleteUserChange(iOrdOrderDto,iUserDto);
				infoManageCrmServiceImpl.deleteUser((IOrdOrder) classList.get(0), (IUser) classList.get(1));
			}
			else {
				log.error("Delete user in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("Delete user in info manage'parameter like iOrdOrder or iUser is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
		}
		return new MvneInfoManageResponse().success();
	}
	
	@PostMapping("changeCard")
	@ResponseBody
	public MvneInfoManageResponse changeCard(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		try {
//			if(null == infoManage) {
//				log.error("Change card in info manage's necessary parameter is null");
//				return new MvneInfoManageResponse().fail().message("Change card in info manage's necessary parameter is null");
//			}
			IOrdOrderDto iOrdOrderDto = infoManage.getIordOrder();
			IUserDto iUserDto = infoManage.getIuser();
			if(null != iOrdOrderDto && null != iUserDto) {
				List<Object> classList = changeDateService.changeCardChange(iOrdOrderDto,iUserDto);
				infoManageCrmServiceImpl.changeCard((IOrdOrder) classList.get(0), (IUser) classList.get(1));
			}
			else {
				log.error("Change card in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("Change card in info manage'parameter like iOrdOrder or iUser is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
		}
		return new MvneInfoManageResponse().success();
	}
}
