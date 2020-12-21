/**
 * 
 */
package com.cmit.mvne.billing.infomanage.controller;

import javax.validation.constraints.NotNull;

import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.entity.*;
import com.cmit.mvne.billing.infomanage.remote.service.CreditControlService;
import com.cmit.mvne.billing.user.analysis.entity.IProd;
import com.cmit.mvne.billing.user.analysis.entity.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.common.MvneInfoManageResponse;
import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
import com.cmit.mvne.billing.user.analysis.entity.IProdDto;
import com.cmit.mvne.billing.user.analysis.entity.IUserDto;
import com.cmit.mvne.billing.infomanage.crm.service.InfoManageCrmService;
import com.cmit.mvne.billing.infomanage.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeoutException;


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
	private CreditControlService creditControlService;
	@Autowired
	private DistributeLock distributeLock;

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public void test(@RequestBody InfoManageCrmEntity infoManageCrmEntity) {
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&");
	}

	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public MvneInfoManageResponse createUser(@RequestBody @NotNull InfoManageCrmEntity infoManageCrmEntity) {
		//String lockKey = null;
		try {

			IOrdOrderDto iOrdOrderDto = infoManageCrmEntity.getIOrdOrder();
			IProdDto iProdDto = infoManageCrmEntity.getIProd();
			IUserDto iUserDto = infoManageCrmEntity.getIUser();
			IProd iProd = null;
			//if(null != iOrdOrderDto && null != iProdDto && null != iUserDto) {
			if(null != iOrdOrderDto && null != iUserDto){
				//List<Object> classList = changeDateService.createUserChange(iOrdOrderDto, iProdDto, iUserDto);
				//IOrdOrder iOrdOrder = (IOrdOrder) classList.get(0);
				IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto);
				IUser iUser = new IUser(iUserDto);
				if (null != iProdDto){
					iProd = new IProd(iProdDto);
				}
				//lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
				infoManageCrmServiceImpl.createUser(iOrdOrder, iProd, iUser);
				//infoManageCrmServiceImpl.createUser((IOrdOrder) classList.get(0), (IProd) classList.get(1), (IUser) classList.get(2));
				log.info("InfoManageController-createUser commit transaction success!");
				//distributeLock.unlock(lockKey);
				//log.info("InfoManageController-createUser unlock success! lockKey is {}",lockKey);
			}
			else {
				log.error("InfoManageController-createUser Create user in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("InfoManageController-createUser Create user in info manage'parameter like iOrdOrder or iProd or iUser is null");
			}
			
		} catch (MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			//distributeLock.unlock(lockKey);
			//log.info("InfoManageController-createUser exception unlock success! lockKey is {}",lockKey);
			//return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
			return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
		}
		return new MvneInfoManageResponse().success();
	}
	
	@PostMapping("changeOffer")
	@ResponseBody
	public MvneInfoManageResponse changeOffer(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		//String lockKey = null;
		try {

			IOrdOrderDto iOrdOrderDto = infoManage.getIOrdOrder();
			IProdDto iProdDto = infoManage.getIProd();
			if(null != iOrdOrderDto && null != iProdDto) {
				//List<Object> classList = changeDateService.changeOfferChange(iOrdOrderDto, iProdDto);
				//IOrdOrder iOrdOrder = (IOrdOrder) classList.get(0);
				IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto);
				IProd iProd = new IProd(iProdDto);
				//lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
				infoManageCrmServiceImpl.changeOffer(iOrdOrder,iProd);
				//creditControlService.CreditControlOfferSms(iOrdOrder,iProd);
				log.info("InfoManageController-changeOffer commit transaction success!");
				//distributeLock.unlock(lockKey);
				//log.info("InfoManageController-changeOffer unlock success! lockKey is {}",lockKey);
			}
			else {
				log.error("InfoManageController-changeOffer Change offer in info manage'parameter like iOrdOrder or iProd is null");
				return new MvneInfoManageResponse().fail().message("InfoManageController-changeOffer Change offer in info manage'parameter like iOrdOrder or iProd is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			//distributeLock.unlock(lockKey);
			//log.info("InfoManageController-changeOffer exception unlock success! lockKey is {}",lockKey);
			//return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
			return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
		}
		return new MvneInfoManageResponse().success();
		
	}
	
	@PostMapping("stop")
	@ResponseBody
	public MvneInfoManageResponse stop(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		String lockKey = null;
		try {

			IOrdOrderDto iOrdOrderDto = infoManage.getIOrdOrder();
			IUserDto iUserDto = infoManage.getIUser();
			if(null != iOrdOrderDto && null != iUserDto) {
				//List<Object> classList = changeDateService.stopChange(iOrdOrderDto,iUserDto);
				//IOrdOrder iOrdOrder = (IOrdOrder) classList.get(0);
				IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto);
				IUser iUser = new IUser(iUserDto);
				lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
				infoManageCrmServiceImpl.stop(iOrdOrder, iUser);
				log.info("InfoManageController-stop commit transaction success!");
				distributeLock.unlock(lockKey);
				log.info("InfoManageController-stop unlock success! lockKey is {}",lockKey);
			}
			else {
				log.error("InfoManageController-stop user in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("InfoManageController-stop user in info manage'parameter like iOrdOrder or iUser is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			distributeLock.unlock(lockKey);
			log.info("InfoManageController-stop exception unlock success! lockKey is {}",lockKey);
			//return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
			return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
		}
		return new MvneInfoManageResponse().success();
	}

	@PostMapping("start")
	@ResponseBody
	public MvneInfoManageResponse start(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		String lockKey = null;
		try {

			IOrdOrderDto iOrdOrderDto = infoManage.getIOrdOrder();
			IUserDto iUserDto = infoManage.getIUser();
			if(null != iOrdOrderDto && null != iUserDto) {
				//List<Object> classList = changeDateService.startChange(iOrdOrderDto,iUserDto);
				//IOrdOrder iOrdOrder = (IOrdOrder) classList.get(0);
				IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto);
				IUser iUser = new IUser(iUserDto);
				lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
				infoManageCrmServiceImpl.start(iOrdOrder, iUser);
				log.info("InfoManageController-start commit transaction success!");
				distributeLock.unlock(lockKey);
				log.info("InfoManageController-start unlock success! lockKey is {}",lockKey);
			}
			else {
				log.error("InfoManageController-start user in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("InfoManageController-start user in info manage'parameter like iOrdOrder or iUser is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			distributeLock.unlock(lockKey);
			log.info("InfoManageController-start exception unlock success! lockKey is {}",lockKey);
			//return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
			return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
		}
		return new MvneInfoManageResponse().success();
	}
	
	@PostMapping("reCharge")
	@ResponseBody
	public MvneInfoManageResponse reCharge(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		//String lockKey = null;
		try {

			IOrdOrderDto iOrdOrderDto = infoManage.getIOrdOrder();
			if(null != iOrdOrderDto) {
				//List<Object> classList = changeDateService.reChargeChange(iOrdOrderDto);
				//IOrdOrder iOrdOrder = (IOrdOrder) classList.get(0);
				IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto);
				//lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
				infoManageCrmServiceImpl.recharge(iOrdOrder);
				//creditControlService.CreditControlChargeSms(iOrdOrder);
				log.info("InfoManageController-reCharge commit transaction success!");
				//distributeLock.unlock(lockKey);
				//log.info("InfoManageController-reCharge unlock success! lockKey is {}",lockKey);
			}
			else {
				log.error("InfoManageController-reCharge in info manage'parameter like iOrdOrder is null");
				return new MvneInfoManageResponse().fail().message("InfoManageController-reCharge in info manage'parameter like iOrdOrder is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			//distributeLock.unlock(lockKey);
			//log.info("InfoManageController-reCharge exception unlock success! lockKey is {}",lockKey);
			//return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
			return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
		}
		return new MvneInfoManageResponse().success();
	}
	
	@PostMapping("deleteUser")
	@ResponseBody
	public MvneInfoManageResponse deleteUser(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		String lockKey = null;
		try {

			IOrdOrderDto iOrdOrderDto = infoManage.getIOrdOrder();
			IUserDto iUserDto = infoManage.getIUser();
			if(null != iOrdOrderDto && null != iUserDto) {
				//List<Object> classList = changeDateService.deleteUserChange(iOrdOrderDto,iUserDto);
				//IOrdOrder iOrdOrder = (IOrdOrder) classList.get(0);
				IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto);
				IUser iUser = new IUser(iUserDto);
				lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
				infoManageCrmServiceImpl.deleteUser(iOrdOrder, iUser);
				log.info("InfoManageController-deleteUser commit transaction success!");
				distributeLock.unlock(lockKey);
				log.info("InfoManageController-deleteUser unlock success! lockKey is {}",lockKey);
			}
			else {
				log.error("InfoManageController-deleteUser user in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("InfoManageController-deleteUser user in info manage'parameter like iOrdOrder or iUser is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			distributeLock.unlock(lockKey);
			log.info("InfoManageController-deleteUser exception unlock success! lockKey is {}",lockKey);
			//return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
			return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
		}
		return new MvneInfoManageResponse().success();
	}
	
	@PostMapping("changeCard")
	@ResponseBody
	public MvneInfoManageResponse changeCard(@RequestBody @NotNull InfoManageCrmEntity infoManage) {
		String lockKey = null;
		try {

			IOrdOrderDto iOrdOrderDto = infoManage.getIOrdOrder();
			IUserDto iUserDto = infoManage.getIUser();
			if(null != iOrdOrderDto && null != iUserDto) {
				//List<Object> classList = changeDateService.changeCardChange(iOrdOrderDto,iUserDto);
				//IOrdOrder iOrdOrder = (IOrdOrder) classList.get(0);
				IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto);
				IUser iUser = new IUser(iUserDto);
				lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
				infoManageCrmServiceImpl.changeCard(iOrdOrder,iUser);
				log.info("InfoManageController-changeCard commit transaction success!");
				distributeLock.unlock(lockKey);
				log.info("InfoManageController-changeCard unlock success! lockKey is {}",lockKey);
			}
			else {
				log.error("InfoManageController-changeCard in info manage'parameter like iOrdOrder or iUser is null");
				return new MvneInfoManageResponse().fail().message("InfoManageController-changeCard in info manage'parameter like iOrdOrder or iUser is null");
			}
		}catch(MvneException e) {
			log.error(StringUtils.getExceptionText(e));
			distributeLock.unlock(lockKey);
			log.info("InfoManageController-changeCard exception unlock success! lockKey is {}",lockKey);
			//return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
			return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
		}
		return new MvneInfoManageResponse().success();
	}
}
