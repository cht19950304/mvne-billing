package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.user.analysis.entity.PmProduct;
import com.cmit.mvne.billing.user.analysis.entity.SysMeasureUnitExchange;
import com.cmit.mvne.billing.user.analysis.mapper.SysMeasureUnitExchangeMapper;
import com.cmit.mvne.billing.user.analysis.service.SysMeasureUnitExchangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
@Service
public class SysMeasureUnitExchangeServiceImpl extends ServiceImpl<SysMeasureUnitExchangeMapper, SysMeasureUnitExchange> implements SysMeasureUnitExchangeService {
    @Autowired
    SysMeasureUnitExchangeMapper sysMeasureUnitExchangeMapper;

    @Override
    @CachePut(value = "SysMeasureUnitExchange", key = "#p0.measureId + ':' + #p0.destMeasureId", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public SysMeasureUnitExchange cache(SysMeasureUnitExchange sysMeasureUnitExchange) {
        return sysMeasureUnitExchange;
    }

    @Override
    @Cacheable(value = "SysMeasureUnitExchange", key = "#p0 + ':' + #p1", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public SysMeasureUnitExchange exchange(BigDecimal measureId, BigDecimal destMeasureId) {
        LambdaQueryWrapper<SysMeasureUnitExchange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMeasureUnitExchange::getMeasureId, measureId).eq(SysMeasureUnitExchange::getDestMeasureId, destMeasureId);
        return sysMeasureUnitExchangeMapper.selectOne(queryWrapper);
    }
}
