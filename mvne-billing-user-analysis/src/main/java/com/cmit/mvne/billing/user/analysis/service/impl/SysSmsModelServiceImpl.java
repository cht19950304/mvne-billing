package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.user.analysis.entity.SysSmsModel;
import com.cmit.mvne.billing.user.analysis.mapper.SysSmsModelMapper;
import com.cmit.mvne.billing.user.analysis.service.SysSmsModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-06-10
 */
@Service
public class SysSmsModelServiceImpl extends ServiceImpl<SysSmsModelMapper, SysSmsModel> implements SysSmsModelService {
    @Autowired
    SysSmsModelMapper sysSmsModelMapper;

    @Override
    @CachePut(value = "SysSmsModel", key = "#p0.reason + ':' + #p0.operation", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public SysSmsModel cacheSysSmsModel(SysSmsModel sysSmsModel) {
        return sysSmsModel;
    }

    @Override
    @Cacheable(value = "SysSmsModel", key = "#p0 + ':' + #p1", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public SysSmsModel selectByReasonAndOperation(String reason, String operation) {
        LambdaQueryWrapper<SysSmsModel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysSmsModel::getOperation, operation)
                .eq(SysSmsModel::getReason, reason);
        return sysSmsModelMapper.selectOne(lambdaQueryWrapper);
    }
}
