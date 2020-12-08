package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.user.analysis.entity.PmProduct;
import com.cmit.mvne.billing.user.analysis.entity.SysRoamZoneGroup;
import com.cmit.mvne.billing.user.analysis.mapper.PmProductMapper;
import com.cmit.mvne.billing.user.analysis.service.PmProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
@Service
public class PmProductServiceImpl extends ServiceImpl<PmProductMapper, PmProduct> implements PmProductService {
    @Autowired
    PmProductMapper pmProductMapper;

    @Override
    @CachePut(value = "PmProduct", key = "#p0.productOfferingId", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public PmProduct cache(PmProduct pmProduct) {
        return pmProduct;
    }

    @Override
    @CachePut(value = "PmProductName", key = "#p0.productOfferingId", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public String cacheName(PmProduct pmProduct) {
        return pmProduct.getName();
    }

    @Override
    public Long create(PmProduct pmProduct) {
        Long productId = pmProductMapper.create(pmProduct);
        return productId;
    }

    @Override
    @Cacheable(value = "PmProduct", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public PmProduct select(Long productId) {
        return pmProductMapper.selectById(productId);
    }

    @Override
    @Cacheable(value = "PmProductName", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public String selectByProductId(Long productId) {
       return pmProductMapper.selectByProductId(productId);
    }
}
