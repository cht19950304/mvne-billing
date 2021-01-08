package com.cmit.mvne.billing.rating.gprs.cache;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.rating.gprs.service.BureauCacheService;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/4/26 16:21
 */

@Slf4j
@Component
public class BureauCacheManager implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    BureauCacheService bureauCacheService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        load();
        log.info("Load cache succeed!");
    }

    /**
     * 启动时加载所有局数据到缓存
     * @return
     */
    public String load() {
        bureauCacheService.cleanSysRoamZoneGroup();
        bureauCacheService.cacheSysRoamZoneGroup();

        bureauCacheService.cleanSysMeasureUnitExchange();
        bureauCacheService.cacheSysMeasureUnitExchange();

        bureauCacheService.cleanRatingRate();
        bureauCacheService.cacheRatingRate();

        bureauCacheService.cleanPmProduct();
        bureauCacheService.cachePmProduct();

        bureauCacheService.cleanBdOperatorCode();
        bureauCacheService.cacheBdOperatorCode();

        bureauCacheService.cleanSysSmsModel();
        bureauCacheService.cacheSysSmsModel();

        return "success";
    }

    /**
     * 产品在创建和失效的时候会刷新
     * 运营商通过局数据每天定时刷新
     * 基础资费也是在产品创建的时候刷新
     * 适用区域在更新后，通过后台接口刷新缓存
     * 单位默认不会被修改
     * @return
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "PmProduct", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager"),
            @CacheEvict(cacheNames = "PmProductName", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager"),
            @CacheEvict(cacheNames = "BdOperatorCode", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager"),
            @CacheEvict(cacheNames = "RatingRate", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager"),
            @CacheEvict(cacheNames = "SysRoamZoneGroup", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager"),
            @CacheEvict(cacheNames = "SysMeasureUnitExchange", allEntries = true, beforeInvocation = true, cacheManager = "selectRedisCacheManager")
    })
    public String cleanUp() {
        return "success";
    }


}
