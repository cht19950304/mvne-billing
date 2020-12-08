package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.user.analysis.entity.PmProduct;
import com.cmit.mvne.billing.user.analysis.entity.RatingRate;
import com.cmit.mvne.billing.user.analysis.entity.SysRoamZoneGroup;
import com.cmit.mvne.billing.user.analysis.mapper.SysRoamZoneGroupMapper;
import com.cmit.mvne.billing.user.analysis.service.SysRoamZoneGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class SysRoamZoneGroupServiceImpl extends ServiceImpl<SysRoamZoneGroupMapper, SysRoamZoneGroup> implements SysRoamZoneGroupService {
    @Autowired
    SysRoamZoneGroupMapper sysRoamZoneGroupMapper;

    /*@Override
    @CachePut(value = "SysRoamZoneGroup", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public List<SysRoamZoneGroup> cache(Integer countryId, List<SysRoamZoneGroup> sysRoamZoneGroupList) {
        return sysRoamZoneGroupList;
    }

    @Override
    @Cacheable(value = "SysRoamZoneGroup", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public List<SysRoamZoneGroup> selectByCountryAndDate(int countryId) {
        LambdaQueryWrapper<SysRoamZoneGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoamZoneGroup::getCountryId, countryId);
        //queryWrapper.ge(SysRoamZoneGroup::getExpireDate, orderDate);
        //queryWrapper.le(SysRoamZoneGroup::getValidDate, orderDate);
        List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupMapper.selectList(queryWrapper);
        return sysRoamZoneGroupList;
    }*/

    @Override
    @Cacheable(value = "SysRoamZoneGroup", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public List<SysRoamZoneGroup> selectByISO(String iso) {
        LambdaQueryWrapper<SysRoamZoneGroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoamZoneGroup::getIso, iso);
        List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupMapper.selectList(lambdaQueryWrapper);
        return sysRoamZoneGroupList;
    }

    @Override
    @CachePut(value = "SysRoamZoneGroup", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public List<SysRoamZoneGroup> cache(String iso, List<SysRoamZoneGroup> sysRoamZoneGroupList) {
        return sysRoamZoneGroupList;
    }


}
