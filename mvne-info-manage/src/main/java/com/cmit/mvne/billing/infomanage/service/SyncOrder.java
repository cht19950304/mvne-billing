package com.cmit.mvne.billing.infomanage.service;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;

/**
 * @Author: caikunchi
 * @Description: 处理不同业务订单的抽象类
 * @Date: Create in 2019/12/4 17:07
 */
public abstract class SyncOrder {
    public void sync(IOrdOrder iOrdOrder) throws MvneException {

    }
}
