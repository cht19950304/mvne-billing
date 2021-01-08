package com.cmit.mvne.billing.rating.gprs.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmit.mvne.billing.rating.gprs.cache.BureauCacheManager;
import com.cmit.mvne.billing.rating.gprs.cache.CaffeineObjectMapper;
import com.cmit.mvne.billing.rating.gprs.service.BureauCacheService;
import com.cmit.mvne.billing.user.analysis.cache.LocalCaffeineCacheManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/4/26 17:05
 */

@Slf4j
@RestController
@RequestMapping("/bureauCacheManagement")
public class BureauCacheManagementController {

    @Autowired
    BureauCacheService bureauCacheService;

    /*@Autowired
    @SuppressWarnings("all")
    LocalCaffeineCacheManager localCaffeineCacheManager;*/

    /**
     * 查询所有缓存的名称
     * @return
     */
    /*@RequestMapping(value = "/queryBureauCacheName", method = RequestMethod.POST)
    Collection<String> queryBureauCacheName() {
        log.info("Query bureau cache name!");
        Collection<String> cacheNames = localCaffeineCacheManager.getCacheNames();

        return cacheNames;
    }*/

    /**
     * 根据缓存名称从缓存中查询数据
     * 一般用来刷新缓存后查询数据是否刷入缓存
     * @param tableName
     * @return
     * @throws ClassNotFoundException
     */
    /*@RequestMapping(value = "/queryBureauCache", method = RequestMethod.POST)
    JSONObject queryBureauCache(@RequestParam(value="tableName") String tableName) throws JsonProcessingException {
        log.info("Query cache: '{}'", tableName);
        CaffeineObjectMapper caffeineObjectMapper = new CaffeineObjectMapper();
        if ("all".equals(tableName)) {
            Map<String, ConcurrentMap> cacheMap = localCaffeineCacheManager.getCacheNames().stream()
                    .collect(Collectors.toMap(Function.identity(), name -> {
                        Cache cache = (Cache) localCaffeineCacheManager.getCache(name).getNativeCache();
                        return cache.asMap();
                    }));
            return JSONObject.parseObject(caffeineObjectMapper.writeValueAsString(cacheMap));
        } else {
            if (localCaffeineCacheManager.getCacheNames().contains(tableName)) {
                Cache cache = (Cache) localCaffeineCacheManager.getCache(tableName).getNativeCache();
                Map<String, ConcurrentMap> cacheMap = new HashMap<String, ConcurrentMap>();
                cacheMap.put(tableName, cache.asMap());
                return JSONObject.parseObject(caffeineObjectMapper.writeValueAsString(cacheMap));
            } else {
                return null;
            }
        }

    }*/

    /**
     * 在刷新的时候，只需要确保数据已经清除即可
     * 如果没有刷新进去，只是影响前面话单的处理效率
     * 可以指定刷新某一张表
     */
    @RequestMapping(value = "/updateAllBureauCache", method = RequestMethod.POST)
    String updateAllBureauCache(@RequestBody List<String> tableNames) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (tableNames.contains("PmProduct") || tableNames.contains("PmProductName")) {
            reload("PmProduct");
        }
        if (tableNames.contains("BdOperatorCode")) {
            reload("BdOperatorCode");
        }
        if (tableNames.contains("SysRoamZoneGroup")) {
            reload("SysRoamZoneGroup");
        }
        if (tableNames.contains("SysMeasureUnitExchange")) {
            reload("SysMeasureUnitExchange");
        }
        if (tableNames.contains("RatingRate")) {
            reload("RatingRate");
        }

        return "success";
    }

    private Boolean reload(String tableName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // 清理缓存
        String cleanMethod = "clean" + tableName;
        Method mClean = bureauCacheService.getClass().getDeclaredMethod(cleanMethod);
        mClean.invoke(bureauCacheService);

        // 重新加载
        String cacheMethod = "cache" + tableName;
        Method mCache = bureauCacheService.getClass().getDeclaredMethod(cacheMethod);
        mCache.invoke(bureauCacheService);

        return true;
    }

    /**
     * 根据tableName更新指定缓存
     * @param tableName
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/updateBureauCache", method = RequestMethod.POST)
    String updateBureauCache(@RequestParam(value="tableName") String tableName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (reload(tableName)) {
            return "success";
        } else {
            return "false";
        }
    }
}
