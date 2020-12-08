/**
 * 
 */
package com.cmit.mvne.billing.infomanage.crm.service;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.entity.IProd;
import com.cmit.mvne.billing.infomanage.entity.IUser;

/**
 * @author jiangxm02
 *
 */
public interface InfoManageCrmService {

	void createUser(IOrdOrder iOrdOrder,IProd iProd,IUser iUser) throws MvneException;
	
	void changeOffer(IOrdOrder iOrdOrder,IProd iProd) throws MvneException;
	
	void stop(IOrdOrder iOrdOrder,IUser iUser) throws MvneException;

	void start(IOrdOrder iOrdOrder,IUser iUser) throws MvneException;
	
	void recharge(IOrdOrder iOrdOrder) throws MvneException;
	
	void deleteUser(IOrdOrder iOrdOrder,IUser iUser) throws MvneException;
	
	void changeCard(IOrdOrder iOrdOrder,IUser iUser) throws MvneException;
}
