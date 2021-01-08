package com.cmit.mvne.billing.rating.gprs.remote.service;

/**
 * @author : huwei
 * @version V1.0
 * @Project: mvne-crm-root
 * @Package com.cmit.mvne.crm.order.remote
 * @Description: TODO
 * @date Date : 2019年12月23日 18:41
 */

import com.cmit.mvne.billing.rating.gprs.creditcontrol.MvneCrmResponse;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.SmsGatewayDto;
import com.cmit.mvne.billing.user.analysis.common.MvneException;

import java.util.List;


public interface CreditControlService {

	MvneCrmResponse sms(SmsGatewayDto smsGatewayDto) throws MvneException;

	MvneCrmResponse smsList(List<SmsGatewayDto> smsGatewayDtoList) throws MvneException;
}
