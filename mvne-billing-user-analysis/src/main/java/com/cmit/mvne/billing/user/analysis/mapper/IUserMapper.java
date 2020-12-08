package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.IUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2019-12-10
 */
public interface IUserMapper extends BaseMapper<IUser> {
    /**
     * 根据order_id查询
     * @param orderId
     * @return
     */
    List<IUser> selectByOrderId(Long orderId);
    
    List<IUser> selectByOrderIdAndUserId(Long orderId,Long userId);
}
