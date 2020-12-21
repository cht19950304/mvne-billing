package com.cmit.mvne.billing.infomanage.remote.service;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.user.analysis.entity.IProd;
import com.cmit.mvne.billing.user.analysis.entity.IUser;

public interface CreditControlService {
    void CreditControlChargeSms(IOrdOrder iOrdOrder) throws MvneException;
    void CreditControlOfferSms(IOrdOrder iOrdOrder, IProd iProd) throws MvneException;
    void CreditControlChangeCardSms(IOrdOrder iOrdOrder, IUser iUser) throws MvneException;
}
