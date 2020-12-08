package com.cmit.mvne.billing.infomanage.crm.service;

import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IProdDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IUserDto;

import java.util.List;

public interface ChangeDateService {
    List<Object> createUserChange(IOrdOrderDto iOrdOrderDto, IProdDto iProdDto, IUserDto iUserDto);
    List<Object> changeOfferChange(IOrdOrderDto iOrdOrderDto, IProdDto iProdDto);
    List<Object> stopChange(IOrdOrderDto iOrdOrderDto,IUserDto iUserDto);
    List<Object> startChange(IOrdOrderDto iOrdOrderDto,IUserDto iUserDto);
    List<Object> reChargeChange(IOrdOrderDto iOrdOrderDto);
    List<Object> deleteUserChange(IOrdOrderDto iOrdOrderDto,IUserDto iUserDto);
    List<Object> changeCardChange(IOrdOrderDto iOrdOrderDto,IUserDto iUserDto);
}
