package com.cmit.mvne.billing.rating.gprs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.rating.gprs.service.BureauCacheService;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/6/12 11:53
 */
@Slf4j
@Service
public class BureauCacheServiceImpl implements BureauCacheService {
    @Autowired
    PmProductService pmProductService;
    @Autowired
    RatingRateService ratingRateService;
    @Autowired
    BdOperatorCodeService bdOperatorCodeService;
    @Autowired
    SysRoamZoneGroupService sysRoamZoneGroupService;
    @Autowired
    SysMeasureUnitExchangeService sysMeasureUnitExchangeService;
    @Autowired
    SysSmsModelService sysSmsModelService;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "SysSmsModel", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager")
    })
    public String cleanSysSmsModel() {
        log.info("Clean up SysSmsModel cache!");
        return "success";
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "PmProduct", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager"),
            @CacheEvict(cacheNames = "PmProductName", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager")
    })
    public String cleanPmProduct() {
        log.info("Clean up PmProduct cache!");
        return "success";
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "BdOperatorCode", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager"),
    })
    public String cleanBdOperatorCode() {
        log.info("Clean up BdOperatorCode cache!");
        return "success";
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "RatingRate", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager")
    })
    public String cleanRatingRate() {
        log.info("Clean up RatingRate cache!");
        return "success";
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "SysRoamZoneGroup", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager"),
    })
    public String cleanSysRoamZoneGroup() {
        log.info("Clean up SysRoamZoneGroup cache!");
        return "success";
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "SysMeasureUnitExchange", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager")
    })
    public String cleanSysMeasureUnitExchange() {
        log.info("Clean up SysMeasureUnitExchange cache!");
        return "success";
    }

    @Override
    public String cacheSysSmsModel() {
        log.info("Loading SysSmsModel into cache...");
        LambdaQueryWrapper<SysSmsModel> queryWrapper = new LambdaQueryWrapper<>();
        List<SysSmsModel> sysSmsModelList = sysSmsModelService.list(queryWrapper);
        for (SysSmsModel sysSmsModel : sysSmsModelList) {
            sysSmsModelService.cacheSysSmsModel(sysSmsModel);
        }
        log.info("Load SysSmsModel succeed!");
        return "Success";
    }

    @Override
    public String cachePmProduct() {
        log.info("Loading PmProduct into cache...");
        LambdaQueryWrapper<PmProduct> queryWrapper = new LambdaQueryWrapper<>();
        List<PmProduct> pmProductList = pmProductService.list(queryWrapper);
        for (PmProduct pmProduct : pmProductList) {
            pmProductService.cache(pmProduct);
            pmProductService.cacheName(pmProduct);
        }
        log.info("Load PmProduct succeed!");
        return "Success";
    }

    @Override
    public String cacheBdOperatorCode() {
        log.info("Loading BdOperatorCode into cache...");
        LambdaQueryWrapper<BdOperatorCode> queryWrapper = new LambdaQueryWrapper<>();
        List<BdOperatorCode> bdOperatorCodeList = bdOperatorCodeService.list(queryWrapper);
        for (BdOperatorCode bdOperatorCode : bdOperatorCodeList) {
            bdOperatorCodeService.cache(bdOperatorCode);
            //bdOperatorCodeService.cacheName(countryOperator);
        }
        log.info("Load BdOperatorCode succeed!");
        return "Success";
    }

    @Override
    public String cacheRatingRate() {
        log.info("Loading RatingRate into cache...");
        HashMap<Long, List<RatingRate>> ratingRateMap = new HashMap<>();

        LambdaQueryWrapper<RatingRate> queryWrapper = new LambdaQueryWrapper<>();
        List<RatingRate> ratingRateList = ratingRateService.list(queryWrapper);
        for (RatingRate ratingRate : ratingRateList) {
            ratingRateService.cacheProductIdAndItemId(ratingRate);
            Long productId = ratingRate.getProductId();
            if (ratingRateMap.containsKey(productId)) {
                List<RatingRate> newRatingRateList = ratingRateMap.get(productId);
                newRatingRateList.add(ratingRate);
                ratingRateMap.put(productId, newRatingRateList);
            } else {
                List<RatingRate> newRatingRateList = new ArrayList<>();
                newRatingRateList.add(ratingRate);
                ratingRateMap.put(productId, newRatingRateList);
            }
        }

        for (Long productId : ratingRateMap.keySet()) {
            ratingRateService.cacheProductId(productId, ratingRateMap.get(productId));
        }
        log.info("Load RatingRate succeed!");
        return "Success";
    }

    @Override
    public String cacheSysRoamZoneGroup() {
        log.info("Loading SysRoamZoneGroup into cache...");
        HashMap<String, List<SysRoamZoneGroup>> sysRoamZoneGroupMap = new HashMap<>();

        LambdaQueryWrapper<SysRoamZoneGroup> queryWrapper = new LambdaQueryWrapper<>();
        List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupService.list(queryWrapper);
        for (SysRoamZoneGroup sysRoamZoneGroup : sysRoamZoneGroupList) {
            String iso = sysRoamZoneGroup.getIso();
            if (sysRoamZoneGroupMap.containsKey(iso)) {
                List<SysRoamZoneGroup> newSysRoamZoneGroupList = sysRoamZoneGroupMap.get(iso);
                newSysRoamZoneGroupList.add(sysRoamZoneGroup);
                sysRoamZoneGroupMap.put(iso, newSysRoamZoneGroupList);
            } else {
                List<SysRoamZoneGroup> newSysRoamZoneGroupList = new ArrayList<>();
                newSysRoamZoneGroupList.add(sysRoamZoneGroup);
                sysRoamZoneGroupMap.put(iso, newSysRoamZoneGroupList);
            }
        }

        for (String iso : sysRoamZoneGroupMap.keySet()) {
            sysRoamZoneGroupService.cache(iso, sysRoamZoneGroupMap.get(iso));
        }
        log.info("Load SysRoamZoneGroup succeed!");
        return "Success";
    }

    @Override
    public String cacheSysMeasureUnitExchange() {
        log.info("Loading SysMeasureUnitExchange into cache...");
        LambdaQueryWrapper<SysMeasureUnitExchange> queryWrapper = new LambdaQueryWrapper<>();
        List<SysMeasureUnitExchange> sysMeasureUnitExchangeList = sysMeasureUnitExchangeService.list(queryWrapper);
        for (SysMeasureUnitExchange sysMeasureUnitExchange : sysMeasureUnitExchangeList) {
            sysMeasureUnitExchangeService.cache(sysMeasureUnitExchange);
        }
        log.info("Load SysMeasureUnitExchange succeed!");
        return "Success";
    }
}
