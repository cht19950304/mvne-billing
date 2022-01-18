package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.mvne.billing.user.analysis.entity.PmProduct;
import com.cmit.mvne.billing.user.analysis.entity.RatingRate;
import com.cmit.mvne.billing.user.analysis.mapper.RatingRateMapper;
import com.cmit.mvne.billing.user.analysis.service.RatingRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * VIEW ����ʵ����
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
@Service
public class RatingRateServiceImpl extends ServiceImpl<RatingRateMapper, RatingRate> implements RatingRateService {
    @Autowired
    RatingRateMapper ratingRateMapper;

    @Override
    @CachePut(value = "RatingRate", key = "#p0.productId + ':' + #p0.itemId", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public RatingRate cacheProductIdAndItemId(RatingRate ratingRate) {
        return ratingRate;
    }

    @Override
    @CachePut(value = "RatingRate", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public List<RatingRate> cacheProductId(Long productId, List<RatingRate> ratingRate) {
        return ratingRate;
    }

    @Override
    @Cacheable(value = "RatingRate", key = "#p0 + ':' + #p1", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public RatingRate selectByKey(Long productId, Long itemId) {
        LambdaQueryWrapper<RatingRate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RatingRate::getProductId, productId).eq(RatingRate::getItemId, itemId);
        return ratingRateMapper.selectOne(queryWrapper);
    }

     /**
     * 根据productId查询所有科目的的套外费用
     * @param productId
     * @return
     */
    @Override
    @Cacheable(value = "RatingRate", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public List<RatingRate> selectByProductId(Long productId) {
        LambdaQueryWrapper<RatingRate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RatingRate::getProductId, productId);
        return ratingRateMapper.selectList(queryWrapper);
    }
}
