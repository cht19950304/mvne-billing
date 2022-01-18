package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.fastjson.annotation.JSONType;
import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
import com.cmit.mvne.billing.user.analysis.entity.IProdDto;
import com.cmit.mvne.billing.user.analysis.entity.IUserDto;

import lombok.Data;

@Data
@JSONType(orders={"iOrdOrder","iProd","iUser"})
public class InfoManageCrmEntity {

	private IOrdOrderDto iOrdOrder;
	private IProdDto iProd;
	private IUserDto iUser;

	
}
