package com.cmit.mvne.billing.rating.gprs.service;


/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/6/12 11:52
 */
public interface BureauCacheService {
    public String cleanSysSmsModel();

    public String cleanPmProduct();

    public String cleanBdOperatorCode();

    public String cleanRatingRate();

    public String cleanSysRoamZoneGroup();

    public String cleanSysMeasureUnitExchange();

    public String cacheSysSmsModel();

    public String cachePmProduct();

    public String cacheBdOperatorCode();

    public String cacheRatingRate();

    public String cacheSysRoamZoneGroup();

    public String cacheSysMeasureUnitExchange();
}
