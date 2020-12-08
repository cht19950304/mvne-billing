package com.cmit.mvne.billing.infomanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.infomanage.entity.CmUserDetail;
import com.cmit.mvne.billing.infomanage.mapper.CmUserDetailMapper;
import com.cmit.mvne.billing.infomanage.service.CmUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
@Service
public class CmUserDetailServiceImpl extends ServiceImpl<CmUserDetailMapper, CmUserDetail> implements CmUserDetailService {
    @Autowired
    private CmUserDetailMapper cmUserDetailMapper;

    @Override
    public void insert(CmUserDetail cmUserDetail) {
        cmUserDetailMapper.insert(cmUserDetail);
    }

	@Override
	public void updateChangeCard(CmUserDetail cmUserDetail, Long userid) {
		UpdateWrapper<CmUserDetail> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("USER_ID", userid);
		updateWrapper.eq("EXPIRE_DATE","2099-12-31 00:00:00");
		cmUserDetailMapper.update(cmUserDetail, updateWrapper);
	}

    @Override
    public int updateExpire(Long userId,String msisdn, Date expireDate,String userStatus) {
       return cmUserDetailMapper.updateExpire(userId,msisdn,expireDate,userStatus);
    }


}
