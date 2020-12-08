package com.cmit.mvne.billing.infomanage.entity;

import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
import com.cmit.mvne.billing.user.analysis.entity.IProdDto;
import com.cmit.mvne.billing.user.analysis.entity.IUserDto;

import lombok.Data;

@Data
public class InfoManageCrmEntity {

	private IOrdOrderDto iOrdOrder;
	private IProdDto iProd;
	private IUserDto iUser;

	
}
