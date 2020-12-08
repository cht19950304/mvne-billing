package com.cmit.mvne.billing.infomanage.service;

import com.cmit.mvne.billing.infomanage.entity.IUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-10
 */
public interface IUserService extends IService<IUser> {
    /**
     * 根据order_id查询
     * @param orderId
     * @return
     */
    public List<IUser> selectByOrderId(Long orderId);
    
    public List<IUser> selectByOrderIdAndUserId(Long orderId,Long userId);
}
