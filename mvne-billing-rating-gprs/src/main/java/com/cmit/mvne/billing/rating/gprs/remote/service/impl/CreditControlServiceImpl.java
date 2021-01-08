package com.cmit.mvne.billing.rating.gprs.remote.service.impl;

import com.cmit.mvne.billing.rating.gprs.remote.CreditControlClient;
import com.cmit.mvne.billing.rating.gprs.remote.service.CreditControlService;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.MvneCrmResponse;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.SmsGatewayDto;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/21 12:13
 */
@Service
public class CreditControlServiceImpl implements CreditControlService {
    @Autowired
    CreditControlClient creditControlClient;

    @Override
    public MvneCrmResponse sms(SmsGatewayDto smsGatewayDto) throws MvneException {
        MvneCrmResponse response = creditControlClient.sms(smsGatewayDto);
        return response;
    }

    @Override
    public MvneCrmResponse smsList(List<SmsGatewayDto> smsGatewayDtoList) throws MvneException {
        MvneCrmResponse response = creditControlClient.smsList(smsGatewayDtoList);
        return response;
    }
}
