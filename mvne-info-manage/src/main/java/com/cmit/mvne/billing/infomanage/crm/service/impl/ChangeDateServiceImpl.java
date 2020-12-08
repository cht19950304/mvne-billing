package com.cmit.mvne.billing.infomanage.crm.service.impl;

import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IProdDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IUserDto;
import com.cmit.mvne.billing.infomanage.crm.service.ChangeDateService;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.entity.IProd;
import com.cmit.mvne.billing.infomanage.entity.IUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ChangeDateServiceImpl implements ChangeDateService {

    @Override
    public List<Object> createUserChange(IOrdOrderDto iOrdOrderDto, IProdDto iProdDto, IUserDto iUserDto) {
        List<Object> classList= new ArrayList<>();
        Date ord_createDate = new Date();
        ord_createDate.setTime(iOrdOrderDto.getCreateDate());
        Date ord_doneDate = new Date();
        ord_doneDate.setTime(iOrdOrderDto.getDoneDate());
        Date ord_xferDate = new Date();
        ord_xferDate.setTime(iOrdOrderDto.getXferDate());
        IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto.getOrderId(),iOrdOrderDto.getOrderType(),
                iOrdOrderDto.getBusiOperCode(),iOrdOrderDto.getCustId(),iOrdOrderDto.getUserId(),
                iOrdOrderDto.getFactMoney(),ord_createDate,ord_doneDate,ord_xferDate);
        classList.add(iOrdOrder);

        Date prod_validateDate = new Date();
        prod_validateDate.setTime(iProdDto.getValidDate());
        Date prod_expireDate = new Date();
        prod_expireDate.setTime(iProdDto.getExpireDate());
        IProd iProd = new IProd(iProdDto.getOrderId(),iProdDto.getUserId(),iProdDto.getRectype(),iProdDto.getProductFee(),
                iProdDto.getProductId(),iProdDto.getProductInsId(),prod_validateDate,prod_expireDate);
        classList.add(iProd);

        Date user_validateDate = new Date();
        user_validateDate.setTime(iUserDto.getValidDate());
        Date user_expireDate = new Date();
        user_expireDate.setTime(iUserDto.getExpireDate());
        IUser iUser = new IUser(iUserDto.getOrderId(),iUserDto.getUserId(),iUserDto.getAcctId(),
                iUserDto.getCustId(),iUserDto.getMsisdn(),iUserDto.getImsi(),iUserDto.getChannelCode(),
                user_validateDate,user_expireDate,iUserDto.getUserStatus());
        classList.add(iUser);
        return classList;
    }

    @Override
    public List<Object> changeOfferChange(IOrdOrderDto iOrdOrderDto, IProdDto iProdDto) {
        List<Object> classList= new ArrayList<>();
        Date ord_createDate = new Date();
        ord_createDate.setTime(iOrdOrderDto.getCreateDate());
        Date ord_doneDate = new Date();
        ord_doneDate.setTime(iOrdOrderDto.getDoneDate());
        Date ord_xferDate = new Date();
        ord_xferDate.setTime(iOrdOrderDto.getXferDate());
        IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto.getOrderId(),iOrdOrderDto.getOrderType(),
                iOrdOrderDto.getBusiOperCode(),iOrdOrderDto.getCustId(),iOrdOrderDto.getUserId(),
                iOrdOrderDto.getFactMoney(),ord_createDate,ord_doneDate,ord_xferDate);
        classList.add(iOrdOrder);

        Date prod_validateDate = new Date();
        prod_validateDate.setTime(iProdDto.getValidDate());
        Date prod_expireDate = new Date();
        prod_expireDate.setTime(iProdDto.getExpireDate());
        IProd iProd = new IProd(iProdDto.getOrderId(),iProdDto.getUserId(),iProdDto.getRectype(),iProdDto.getProductFee(),
                iProdDto.getProductId(),iProdDto.getProductInsId(),prod_validateDate,prod_expireDate);
        classList.add(iProd);
        return classList;
    }

    @Override
    public List<Object> stopChange(IOrdOrderDto iOrdOrderDto,IUserDto iUserDto) {
        List<Object> classList= new ArrayList<>();
        Date ord_createDate = new Date();
        ord_createDate.setTime(iOrdOrderDto.getCreateDate());
        Date ord_doneDate = new Date();
        ord_doneDate.setTime(iOrdOrderDto.getDoneDate());
        Date ord_xferDate = new Date();
        ord_xferDate.setTime(iOrdOrderDto.getXferDate());
        IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto.getOrderId(),iOrdOrderDto.getOrderType(),
                iOrdOrderDto.getBusiOperCode(),iOrdOrderDto.getCustId(),iOrdOrderDto.getUserId(),
                iOrdOrderDto.getFactMoney(),ord_createDate,ord_doneDate,ord_xferDate);
        classList.add(iOrdOrder);

        Date user_validateDate = new Date();
        user_validateDate.setTime(iUserDto.getValidDate());
        Date user_expireDate = new Date();
        user_expireDate.setTime(iUserDto.getExpireDate());
        IUser iUser = new IUser(iUserDto.getOrderId(),iUserDto.getUserId(),iUserDto.getAcctId(),
                iUserDto.getCustId(),iUserDto.getMsisdn(),iUserDto.getImsi(),iUserDto.getChannelCode(),
                user_validateDate,user_expireDate,iUserDto.getUserStatus());
        classList.add(iUser);

        return classList;
    }

    @Override
    public List<Object> startChange(IOrdOrderDto iOrdOrderDto,IUserDto iUserDto) {
        List<Object> classList= new ArrayList<>();
        Date ord_createDate = new Date();
        ord_createDate.setTime(iOrdOrderDto.getCreateDate());
        Date ord_doneDate = new Date();
        ord_doneDate.setTime(iOrdOrderDto.getDoneDate());
        Date ord_xferDate = new Date();
        ord_xferDate.setTime(iOrdOrderDto.getXferDate());
        IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto.getOrderId(),iOrdOrderDto.getOrderType(),
                iOrdOrderDto.getBusiOperCode(),iOrdOrderDto.getCustId(),iOrdOrderDto.getUserId(),
                iOrdOrderDto.getFactMoney(),ord_createDate,ord_doneDate,ord_xferDate);
        classList.add(iOrdOrder);

        Date user_validateDate = new Date();
        user_validateDate.setTime(iUserDto.getValidDate());
        Date user_expireDate = new Date();
        user_expireDate.setTime(iUserDto.getExpireDate());
        IUser iUser = new IUser(iUserDto.getOrderId(),iUserDto.getUserId(),iUserDto.getAcctId(),
                iUserDto.getCustId(),iUserDto.getMsisdn(),iUserDto.getImsi(),iUserDto.getChannelCode(),
                user_validateDate,user_expireDate,iUserDto.getUserStatus());
        classList.add(iUser);
        return classList;

    }

    @Override
    public List<Object> reChargeChange(IOrdOrderDto iOrdOrderDto) {
        List<Object> classList= new ArrayList<>();
        Date ord_createDate = new Date();
        ord_createDate.setTime(iOrdOrderDto.getCreateDate());
        Date ord_doneDate = new Date();
        ord_doneDate.setTime(iOrdOrderDto.getDoneDate());
        Date ord_xferDate = new Date();
        ord_xferDate.setTime(iOrdOrderDto.getXferDate());
        IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto.getOrderId(),iOrdOrderDto.getOrderType(),
                iOrdOrderDto.getBusiOperCode(),iOrdOrderDto.getCustId(),iOrdOrderDto.getUserId(),
                iOrdOrderDto.getFactMoney(),ord_createDate,ord_doneDate,ord_xferDate);
        classList.add(iOrdOrder);
        return classList;

    }

    @Override
    public List<Object> deleteUserChange(IOrdOrderDto iOrdOrderDto,IUserDto iUserDto) {
        List<Object> classList= new ArrayList<>();
        Date ord_createDate = new Date();
        ord_createDate.setTime(iOrdOrderDto.getCreateDate());
        Date ord_doneDate = new Date();
        ord_doneDate.setTime(iOrdOrderDto.getDoneDate());
        Date ord_xferDate = new Date();
        ord_xferDate.setTime(iOrdOrderDto.getXferDate());
        IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto.getOrderId(),iOrdOrderDto.getOrderType(),
                iOrdOrderDto.getBusiOperCode(),iOrdOrderDto.getCustId(),iOrdOrderDto.getUserId(),
                iOrdOrderDto.getFactMoney(),ord_createDate,ord_doneDate,ord_xferDate);
        classList.add(iOrdOrder);

        Date user_validateDate = new Date();
        user_validateDate.setTime(iUserDto.getValidDate());
        Date user_expireDate = new Date();
        user_expireDate.setTime(iUserDto.getExpireDate());
        IUser iUser = new IUser(iUserDto.getOrderId(),iUserDto.getUserId(),iUserDto.getAcctId(),
                iUserDto.getCustId(),iUserDto.getMsisdn(),iUserDto.getImsi(),iUserDto.getChannelCode(),
                user_validateDate,user_expireDate,iUserDto.getUserStatus());
        classList.add(iUser);

        return classList;
    }

    @Override
    public List<Object> changeCardChange(IOrdOrderDto iOrdOrderDto,IUserDto iUserDto) {
        List<Object> classList= new ArrayList<>();
        Date ord_createDate = new Date();
        ord_createDate.setTime(iOrdOrderDto.getCreateDate());
        Date ord_doneDate = new Date();
        ord_doneDate.setTime(iOrdOrderDto.getDoneDate());
        Date ord_xferDate = new Date();
        ord_xferDate.setTime(iOrdOrderDto.getXferDate());
        IOrdOrder iOrdOrder = new IOrdOrder(iOrdOrderDto.getOrderId(),iOrdOrderDto.getOrderType(),
                iOrdOrderDto.getBusiOperCode(),iOrdOrderDto.getCustId(),iOrdOrderDto.getUserId(),
                iOrdOrderDto.getFactMoney(),ord_createDate,ord_doneDate,ord_xferDate);
        classList.add(iOrdOrder);

        Date user_validateDate = new Date();
        user_validateDate.setTime(iUserDto.getValidDate());
        Date user_expireDate = new Date();
        user_expireDate.setTime(iUserDto.getExpireDate());
        IUser iUser = new IUser(iUserDto.getOrderId(),iUserDto.getUserId(),iUserDto.getAcctId(),
                iUserDto.getCustId(),iUserDto.getMsisdn(),iUserDto.getImsi(),iUserDto.getChannelCode(),
                user_validateDate,user_expireDate,iUserDto.getUserStatus());
        classList.add(iUser);
        return classList;
    }
}
