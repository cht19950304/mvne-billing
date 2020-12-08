package com.cmit.mvne.billing.infomanage.entity;

import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IProdDto;
import com.cmit.mvne.billing.infomanage.crm.entity.IUserDto;

import lombok.Data;

@Data
public class InfoManageCrmEntity {

	private IOrdOrderDto iordOrder;
	private IProdDto iprod;
	private IUserDto iuser;

	
}
