package com.cmit.mvne.billing.infomanage.service.impl;

import com.cmit.mvne.billing.infomanage.entity.IUser;
import com.cmit.mvne.billing.infomanage.mapper.IUserMapper;
import com.cmit.mvne.billing.infomanage.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-10
 */
@Service
public class IUserServiceImpl extends ServiceImpl<IUserMapper, IUser> implements IUserService {
    @Autowired
    private IUserMapper iUserMapper;

    @Override
    public List<IUser> selectByOrderId(Long orderId) {
        return iUserMapper.selectByOrderId(orderId);
    }

	@Override
	public List<IUser> selectByOrderIdAndUserId(Long orderId, Long userId) {
		return iUserMapper.selectByOrderIdAndUserId(orderId, userId);
	}
}
