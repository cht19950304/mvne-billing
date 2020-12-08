package com.cmit.mvne.billing.user.analysis.service.impl;

import com.cmit.mvne.billing.user.analysis.entity.ApsBalanceFee;
import com.cmit.mvne.billing.user.analysis.mapper.ApsBalanceFeeMapper;
import com.cmit.mvne.billing.user.analysis.service.ApsBalanceFeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
@Service
public class ApsBalanceFeeServiceImpl extends ServiceImpl<ApsBalanceFeeMapper, ApsBalanceFee> implements ApsBalanceFeeService {

    @Autowired
    private ApsBalanceFeeMapper apsBalanceFeeMapper;

    @Override
    public void insert(ApsBalanceFee apsBalanceFee) {
        apsBalanceFeeMapper.insert(apsBalanceFee);
    }

    @Override
    public ApsBalanceFee selectBalanceFee() {
        return apsBalanceFeeMapper.selectBalanceFee();
    }

    @Override
    public int updateBalance(Long userId, BigDecimal remainFee, Date updateTime) {
        return apsBalanceFeeMapper.updateBalance(userId, remainFee,updateTime);
    }

    @Override
    public ApsBalanceFee selectByUserId(Long userId) {
        return apsBalanceFeeMapper.selectByUserId(userId);
    }
}
