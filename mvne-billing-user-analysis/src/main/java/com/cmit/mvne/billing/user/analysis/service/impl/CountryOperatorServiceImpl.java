package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.user.analysis.entity.CountryOperator;
import com.cmit.mvne.billing.user.analysis.entity.PmProduct;
import com.cmit.mvne.billing.user.analysis.entity.SysRoamZoneGroup;
import com.cmit.mvne.billing.user.analysis.mapper.CountryOperatorMapper;
import com.cmit.mvne.billing.user.analysis.service.CountryOperatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-24
 */
@Service
public class CountryOperatorServiceImpl extends ServiceImpl<CountryOperatorMapper, CountryOperator> implements CountryOperatorService {
    @Autowired
    CountryOperatorMapper countryOperatorMapper;

    @Override
    @CachePut(value = "CountryOperator", key = "#p0.operatorCode", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public CountryOperator cache(CountryOperator countryOperator) {
        return countryOperator;
    }

    @Override
    @CachePut(value = "CountryOperatorName", key = "#p0.operatorCode", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public String cacheName(CountryOperator countryOperator) {
        return countryOperator.getOperatorName();
    }

    @Override
    @Cacheable(value = "CountryOperator", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public CountryOperator selectByOperator(String operator) {
        LambdaQueryWrapper<CountryOperator> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CountryOperator::getOperatorCode, operator);
        // 查出来多个，都是一样的。避免使用selectOne导致异常
        List<CountryOperator> countryOperatorList = countryOperatorMapper.selectList(queryWrapper);
        if (countryOperatorList.size()!=0) {
            return countryOperatorList.get(0);
        } else {
            return null;
        }

    }

    @Override
    @Cacheable(value = "CountryOperatorName", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public String selectByOperCode(String operatorCode) {
        return countryOperatorMapper.selectByOperCode(operatorCode);
    }
}
