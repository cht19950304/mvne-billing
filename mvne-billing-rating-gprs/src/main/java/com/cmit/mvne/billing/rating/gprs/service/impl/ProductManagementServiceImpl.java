package com.cmit.mvne.billing.rating.gprs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.rating.gprs.cache.BureauCacheManager;
import com.cmit.mvne.billing.rating.gprs.common.DateComms;
import com.cmit.mvne.billing.rating.gprs.dto.CountryZoneDto;
import com.cmit.mvne.billing.rating.gprs.service.BureauCacheService;
import com.cmit.mvne.billing.rating.gprs.service.ProductManagementService;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.mapper.CmUserDetailMapper;
import com.cmit.mvne.billing.user.analysis.mapper.CountryZoneMapper;
import com.cmit.mvne.billing.user.analysis.mapper.PmProductMapper;
import com.cmit.mvne.billing.user.analysis.mapper.SysRoamZoneGroupMapper;
import com.cmit.mvne.billing.user.analysis.service.*;
import com.cmit.mvne.billing.rating.gprs.common.MvneCrmResponse;
import com.cmit.mvne.billing.rating.gprs.dto.ProductInfo;
import com.cmit.mvne.billing.rating.gprs.remote.Resource;
import com.cmit.mvne.billing.rating.gprs.util.MeasureExchangeUtils;
import com.cmit.mvne.billing.user.analysis.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/23 11:20
 */
@Slf4j
@Service
public class ProductManagementServiceImpl implements ProductManagementService {
    @Autowired
    PmProductMapper pmProductMapper;
    @Autowired
    PmProductService pmProductService;
    @Autowired
    PmProductPricingPlanService pmProductPricingPlanService;
    @Autowired
    PmCompositeOfferPriceService pmCompositeOfferPriceService;
    @Autowired
    PmComponentProdofferPriceService pmComponentProdofferPriceService;
    @Autowired
    PmAllowanceFreeresSegmentService pmAllowanceFreeresSegmentService;
    @Autowired
    PmFreeUsagePropertyService pmFreeUsagePropertyService;
    @Autowired
    PmAllowanceFreeresDetailsService pmAllowanceFreeresDetailsService;
    @Autowired
    PmRatingTariffDtlService pmRatingTariffDtlService;
    @Autowired
    PmSimpleRatePlanService pmSimpleRatePlanService;
    @Autowired
    PmComponentUsagePriceService pmComponentUsagePriceService;
    @Autowired
    PmRatingCurveAttributeService pmRatingCurveAttributeService;
    @Autowired
    PmComponentSimplePriceService pmComponentSimplePriceService;
    @Autowired
    PmRatingRateService pmRatingRateService;
    @Autowired
    PmRatingCurveDetailService pmRatingCurveDetailService;
    @Autowired
    SysRoamZoneGroupMapper sysRoamZoneGroupMapper;
    @Autowired
    SysRoamZoneGroupService sysRoamZoneGroupService;
    @Autowired
    CmUserDetailMapper cmUserDetailMapper;

    @Autowired
    FreeResService freeResService;
    @Autowired
    RatingRateService ratingRateService;
    @Autowired
    PmProductDupService pmProductDupService;
    @Autowired
    CountryZoneService countryZoneService;
    @Autowired
    CountryZoneMapper countryZoneMapper;

    @Autowired
    BureauCacheManager bureauCacheManager;
    @Autowired
    BureauCacheService bureauCacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MvneCrmResponse createProduct(ProductInfo productInfo) {
        log.info("Creating product: {}", productInfo.toString());

        // 分解信息
        String productName = productInfo.getProductName();
        BigDecimal productFee = productInfo.getProductFee();
        // 最好有zoneid
        String zone = productInfo.getRegionCode();
        // 判断regioncode的有效性及groupid
        Integer groupId = searchCountryZone(zone);
        if (groupId!=null) {
            Resource resource = productInfo.getResource().get(0);
            Resource overFee = productInfo.getOverFee().get(0);
            int effectiveTime = productInfo.getEffectiveTime();
            Date effectiveDate = new Date(productInfo.getEffectiveDate());
            Date expireDate = new Date(productInfo.getExpireDate());

            // 费用单位会传进来，需要转成欧分；计费量直接填
            BigDecimal measureValue_front = new BigDecimal(String.valueOf(overFee.getResourceMap().get("valueMeasure")));
            BigDecimal measureFee_front = new BigDecimal(String.valueOf(overFee.getResourceMap().get("feeMeasure")));
            BigDecimal measureEuroCent = new BigDecimal(10301);
            BigDecimal feeAmount_front = new BigDecimal(String.valueOf(overFee.getResourceMap().get("feeAmount")));
            BigDecimal feeAmount_EuroCent = MeasureExchangeUtils.exchangeFloor(feeAmount_front, measureFee_front, measureEuroCent, 2);
            BigDecimal valueAmount = new BigDecimal(String.valueOf(overFee.getResourceMap().get("valueAmount")));

            // 资源单位会传进来，需要转成B
            BigDecimal measureFreeRes_front = new BigDecimal(String.valueOf(resource.getResourceMap().get("valueMeasure")));
            BigDecimal measureFreeRes_B = new BigDecimal(103);

            BigDecimal valueAmountFree_res_front = new BigDecimal(String.valueOf(resource.getResourceMap().get("valueAmount")));
            BigDecimal valueAmountFreeRes_B = MeasureExchangeUtils.exchangeFloor(valueAmountFree_res_front, measureFreeRes_front, measureFreeRes_B, 0);

            PmProductDup pmProductDup = new PmProductDup(productName, productFee,
                    valueAmountFree_res_front, measureFreeRes_front,
                    feeAmount_front, measureFee_front, valueAmount, measureFee_front,
                    zone, groupId, effectiveTime, effectiveDate, expireDate);
            // 校验是否存在有效的当前产品
            try {
                // 先尝试插入dup表，能插入，没问题
                pmProductDupService.save(pmProductDup);
            } catch (Exception e) {
                // 如果无法插入，说明之前已经有过名字一样的了
                //
                LambdaQueryWrapper<PmProductDup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(PmProductDup::getName, pmProductDup.getName());

                List<PmProductDup> pmProductDupList = pmProductDupService.list(lambdaQueryWrapper);
                // 设了主键，所以只会有一个
                PmProductDup pmProductDup_inTable = pmProductDupList.get(0);
                Long productId_inTable = pmProductDup_inTable.getProductId();
                // 因为之前创建时会将产品id更新到dup表，所以可以查
                LambdaQueryWrapper<PmProduct> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(PmProduct::getProductOfferingId, productId_inTable);
                PmProduct pmProduct_inTable = pmProductService.list(lambdaQueryWrapper1).get(0);
                // 如果已经失效，则可以插入
                if (pmProduct_inTable.getExpireDate().compareTo(new Date()) == -1) {

                } else {
                    return new MvneCrmResponse().fail().data("Already exists same product! Please check product!");
                }

            }

            // 需要插入的表
            PmProduct pmProduct = new PmProduct(productName, productFee, zone, effectiveTime, effectiveDate, expireDate);
            pmProduct.setGroupId(groupId);
            pmProduct.setProdDesc(productInfo.getDesc());
            // 获得主键产品id
            pmProductService.create(pmProduct);
            Long productOfferingId = pmProduct.getProductOfferingId();
            productInfo.setProductId(productOfferingId);

            PmProductPricingPlan pmProductPricingPlan = new PmProductPricingPlan(productOfferingId, 0L, new BigDecimal(500), new BigDecimal(-1), new BigDecimal(0), new BigDecimal(0), productOfferingId);
            Long pricingPlanId = pmProductPricingPlan.getPricingPlanId();
            String order = pricingPlanId.toString().substring(pricingPlanId.toString().length() - 3, pricingPlanId.toString().length());
            Long baseFeePriceId = getBaseFeePriceId(pricingPlanId, order);
            Long freeResPriceId = getFreeResPriceId(pricingPlanId, order);
            PmCompositeOfferPrice pmCompositeOfferPrice_baseFee = new PmCompositeOfferPrice(pricingPlanId, baseFeePriceId, new BigDecimal(-1), new BigDecimal(-1), effectiveDate, expireDate, new BigDecimal(0));
            PmCompositeOfferPrice pmCompositeOfferPrice_freeRes = new PmCompositeOfferPrice(pricingPlanId, freeResPriceId, new BigDecimal(-1), new BigDecimal(-1), effectiveDate, expireDate, new BigDecimal(0));
            PmComponentProdofferPrice pmComponentProdofferPrice_baseFee = new PmComponentProdofferPrice(baseFeePriceId, productOfferingId + "套餐基本资费", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), "yellow一期产品", new BigDecimal(0));
            PmComponentProdofferPrice pmComponentProdofferPrice_freeRes = new PmComponentProdofferPrice(freeResPriceId, productOfferingId + "套餐免费资源", new BigDecimal(3), new BigDecimal(0), new BigDecimal(0), "yellow一期产品", new BigDecimal(0));
            ;


            Long itemId = 66020001L;
            PmAllowanceFreeresSegment pmAllowanceFreeresSegment = new PmAllowanceFreeresSegment(baseFeePriceId, new BigDecimal(1), new BigDecimal(1), valueAmountFreeRes_B, new BigDecimal(0), new BigDecimal(-1), new BigDecimal(0));
            PmFreeUsageProperty pmFreeUsageProperty = new PmFreeUsageProperty(itemId, measureFreeRes_B, -1L, new BigDecimal(1), new BigDecimal(-1), new BigDecimal(1), new BigDecimal(0));
            PmAllowanceFreeresDetails pmAllowanceFreeresDetails = new PmAllowanceFreeresDetails(baseFeePriceId, itemId, 0L, new BigDecimal(0), new BigDecimal(0), null, new BigDecimal(3), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));


            Long tariffDtlId = baseFeePriceId;
            Long usageId = tariffDtlId;
            Long curveAttributeId = 1000L;
            Long curveId = usageId;
            Long rateId = usageId;
            PmRatingTariffDtl pmRatingTariffDtl = new PmRatingTariffDtl(tariffDtlId, baseFeePriceId, itemId, new BigDecimal(0), new BigDecimal(0), new BigDecimal(1));
            PmSimpleRatePlan pmSimpleRatePlan = new PmSimpleRatePlan(tariffDtlId, usageId, new BigDecimal(1));
            PmComponentUsagePrice pmComponentUsagePrice = new PmComponentUsagePrice(usageId, curveAttributeId, new BigDecimal(0), new BigDecimal(1));
            PmRatingCurveAttribute pmRatingCurveAttribute = new PmRatingCurveAttribute(curveAttributeId, "每MB", valueAmount, new BigDecimal(1), feeAmount_front + "欧元每MB", new BigDecimal(0), new BigDecimal(0), measureValue_front);
            PmComponentSimplePrice pmComponentSimplePrice = new PmComponentSimplePrice(usageId, rateId, new BigDecimal(0), new BigDecimal(1));
            PmRatingRate pmRatingRate = new PmRatingRate(rateId, feeAmount_front + "欧元每MB", curveId, new BigDecimal(0), feeAmount_front + "欧元每MB", new BigDecimal(1), measureEuroCent);
            PmRatingCurveDetail pmRatingCurveDetail = new PmRatingCurveDetail(curveId, 1L, new BigDecimal(1), new BigDecimal(-1), new BigDecimal(0), feeAmount_EuroCent, new BigDecimal(1), new BigDecimal(0));


            // 开始插表
            pmProductPricingPlanService.save(pmProductPricingPlan);

            pmCompositeOfferPriceService.save(pmCompositeOfferPrice_baseFee);
            pmCompositeOfferPriceService.save(pmCompositeOfferPrice_freeRes);

            pmComponentProdofferPriceService.save(pmComponentProdofferPrice_baseFee);
            pmComponentProdofferPriceService.save(pmComponentProdofferPrice_freeRes);

            pmAllowanceFreeresSegmentService.save(pmAllowanceFreeresSegment);

            LambdaQueryWrapper<PmFreeUsageProperty> pmFreeUsagePropertyQueryWrapper = new LambdaQueryWrapper<>();
            pmFreeUsagePropertyQueryWrapper.eq(PmFreeUsageProperty::getAssetItemId, itemId);
            if (pmFreeUsagePropertyService.count(pmFreeUsagePropertyQueryWrapper) == 0) {
                pmFreeUsagePropertyService.save(pmFreeUsageProperty);
            }

            pmAllowanceFreeresDetailsService.save(pmAllowanceFreeresDetails);

            pmRatingTariffDtlService.save(pmRatingTariffDtl);

            pmSimpleRatePlanService.save(pmSimpleRatePlan);

            pmComponentUsagePriceService.save(pmComponentUsagePrice);

            LambdaQueryWrapper<PmRatingCurveAttribute> pmRatingCurveAttributeQueryWrapper = new LambdaQueryWrapper<>();
            pmRatingCurveAttributeQueryWrapper.eq(PmRatingCurveAttribute::getCurveAttributeId, 1000L);
            if (pmRatingCurveAttributeService.count(pmRatingCurveAttributeQueryWrapper) == 0) {
                pmRatingCurveAttributeService.save(pmRatingCurveAttribute);
            }

            pmComponentSimplePriceService.save(pmComponentSimplePrice);

            pmRatingRateService.save(pmRatingRate);

            pmRatingCurveDetailService.save(pmRatingCurveDetail);

            // 更新dup表的productId
            LambdaUpdateWrapper<PmProductDup> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(PmProductDup::getName, pmProductDup.getName())
                    .set(PmProductDup::getProductId, productOfferingId);
            Boolean result = pmProductDupService.update(lambdaUpdateWrapper);

            // 刷新缓存
            // 为什么是刷新呢？因为视图不是直接操作的
            bureauCacheService.cleanPmProduct();
            bureauCacheService.cachePmProduct();
            bureauCacheService.cleanRatingRate();
            bureauCacheService.cacheRatingRate();

            return new MvneCrmResponse().success().data(productOfferingId);
        } else {
            return new MvneCrmResponse().fail().data("Cannot find region code " + zone);
        }
    }

    private Long getBaseFeePriceId(Long pricingPlanId, String order) {
        String term = "1";
        String gprs = "8";
        String fee = "17";
        String baseFee = "00";
        Long priceId = Long.parseLong(term.concat(gprs).concat(fee).concat(baseFee).concat(order));
        return priceId;
    }

    private Long getFreeResPriceId(Long pricingPlanId, String order) {
        String term = "1";
        String gprs = "8";
        String fee = "17";
        String freeRes = "03";
        Long priceId = Long.parseLong(term.concat(gprs).concat(fee).concat(freeRes).concat(order));
        return priceId;
    }

    private Map<String, JSONObject> getResource(String resource) {
        return JSON.parseObject(resource, new HashMap<String, JSONObject>().getClass());
    }

    private Map<String, JSONObject> getOverFee(String overFee) {
        return JSON.parseObject(overFee, new HashMap<String, JSONObject>().getClass());
    }

    @Override
    public MvneCrmResponse expireProduct(Long productId, Long expireDate) {
        log.info("ProductId = {}, expireDate = {}", productId, expireDate);
        LambdaQueryWrapper<PmProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmProduct::getProductOfferingId, productId);
        PmProduct pmProduct = pmProductService.getOne(lambdaQueryWrapper);
        // 规整成没有毫秒数的fasttime
        Date expireTime = new Date(expireDate-expireDate%1000);
        if (pmProduct.getEffectiveDate().compareTo(expireTime) > -1) {
            return new MvneCrmResponse().fail().message("Expire date should be later than effective date.");
        }

        UpdateWrapper<PmProduct> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmProduct> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(PmProduct::getExpireDate, new Date(expireDate))
                .eq(PmProduct::getProductOfferingId, productId);

        Boolean is = pmProductService.update(new PmProduct(productId, expireDate), lambdaUpdateWrapper);
        if (is) {
            // 刷新缓存
            bureauCacheService.cleanPmProduct();
            bureauCacheService.cachePmProduct();
            bureauCacheService.cleanRatingRate();
            bureauCacheService.cacheRatingRate();

            return new MvneCrmResponse().success();
        } else {
            return new MvneCrmResponse().fail().message("Update table failed!");
        }
    }

    @Override
    public MvneCrmResponse searchProduct(String productName, String productStatus, String regionCode, int page, int size) {
        log.info("ProductName = {}, productStatus = {}, regionCode = {}", productName, productStatus, regionCode);
        Date now = new Date();
        String allRegion = "0";
        // 等于当前时间的为过期
        LambdaQueryWrapper<PmProduct> queryWrapper = new LambdaQueryWrapper<>();
        if (!"".equals(productName)) {
            queryWrapper.like(PmProduct::getName, productName);
        }
        if (!"".equals(regionCode)) {
            if (!allRegion.equals(regionCode)){
                queryWrapper.eq(PmProduct::getZoneGroup, regionCode);
            }
        }
        if (!"".equals(productStatus)) {
            // 4-已生效产品：当前时间在生失效时间之间
            // 6-已下架产品：失效时间小于当前时间
            // 1-未生效产品：生效时间大于当前时间
            if (productStatus.contains("4") && productStatus.contains("6") && productStatus.contains("1")) {
                queryWrapper.and(i ->
                        i.nested(j -> j.lt(PmProduct::getEffectiveDate, now).gt(PmProduct::getExpireDate, now)).or().le(PmProduct::getExpireDate, now).or().gt(PmProduct::getEffectiveDate, now));
            } else if (productStatus.contains("4") && productStatus.contains("6")) {
                queryWrapper.and(i ->
                        i.nested(j -> j.lt(PmProduct::getEffectiveDate, now).gt(PmProduct::getExpireDate, now)).or().le(PmProduct::getExpireDate, now));
            } else if (productStatus.contains("1") && productStatus.contains("6")) {
                queryWrapper.and(i ->
                        i.nested(j -> j.gt(PmProduct::getEffectiveDate, now)).or().le(PmProduct::getExpireDate, now));
            } else if (productStatus.contains("1") && productStatus.contains("4")) {
                queryWrapper.and(i ->
                        i.nested(j -> j.lt(PmProduct::getEffectiveDate, now).gt(PmProduct::getExpireDate, now)).or().gt(PmProduct::getEffectiveDate, now));
            } else if (productStatus.contains("4")) {
                queryWrapper.and(i -> i.lt(PmProduct::getEffectiveDate, now).gt(PmProduct::getExpireDate, now));
            } else if (productStatus.contains("6")) {
                queryWrapper.and(i -> i.le(PmProduct::getExpireDate, now));
            } else if (productStatus.contains("1")) {
                queryWrapper.and(i -> i.gt(PmProduct::getEffectiveDate, now));
            }

        }
        log.info(queryWrapper.getSqlSegment());
        Page<PmProduct> pageParam = new Page<>(page, size, true);
        IPage<PmProduct> pmProductPage = pmProductMapper.selectPage(pageParam, queryWrapper);

        List<PmProduct> pmProductList = pmProductPage.getRecords();
        List<ProductInfo> productInfoList = new ArrayList<>();
        IPage<ProductInfo> productInfoListPage = null;
        try {
            for (PmProduct pmProduct : pmProductList) {
                Long productId = pmProduct.getProductOfferingId();
                FreeRes freeRes = freeResService.selectByProductId(productId);
                BigDecimal originResourceMeasure = pmProductDupService.selectByProductId(productId);
                // 如果没查到则默认按MB返回
                originResourceMeasure = (originResourceMeasure == null ? new BigDecimal(105) : originResourceMeasure);
                List<RatingRate> ratingRateList = ratingRateService.selectByProductId(productId);
                RatingRate ratingRate = new RatingRate();
                // 如果没查到ratingrate，说明产品有问题，报错，不返回
                if (ratingRateList.size() == 0) {
                    log.error("Searched product {} but not found rating rate!", productId);
                    continue;
                } else {
                    // 考虑后面区分科目
                    ratingRate = ratingRateList.get(0);
                }
                ProductInfo productInfo = new ProductInfo(now, pmProduct, freeRes, ratingRate, originResourceMeasure);
                productInfoList.add(productInfo);
            }
            productInfoListPage = new Page<>(pmProductPage.getCurrent(), pmProductPage.getSize(), pmProductPage.getTotal());
            productInfoListPage.setRecords(productInfoList);
        } catch (Exception e) {
            log.error(StringUtil.getExceptionText(e));
            return new MvneCrmResponse().fail().message(StringUtil.getExceptionText(e).substring(0, 2047));
        }

        MvneCrmResponse mvneCrmResponse = new MvneCrmResponse();
        mvneCrmResponse.success().data(productInfoListPage);

        Object data = mvneCrmResponse.get("data");
        IPage<ProductInfo> productInfoListPage1 = (IPage<ProductInfo>) data;

        return mvneCrmResponse;
    }

    /**
     * 查询country zone，对外接口
     * @return
     */
    @Override
    public MvneCrmResponse searchCountryZone(Boolean isCreatingProduct) {
        LambdaQueryWrapper<SysRoamZoneGroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (isCreatingProduct) {
            lambdaQueryWrapper.ge(SysRoamZoneGroup::getExpireDate, new Date())
                    .le(SysRoamZoneGroup::getValidDate, new Date());
        }
        List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupService.list(lambdaQueryWrapper);
        List<String> countryGroupList = sysRoamZoneGroupList.stream().map(SysRoamZoneGroup::getCountryGroup).distinct().collect(Collectors.toList());
        return new MvneCrmResponse().success().data(countryGroupList);
    }

    @Override
    public MvneCrmResponse searchCountry(String countryZone, String status, int page, int size) {
        LambdaQueryWrapper<CountryZone> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (countryZone.equals("All") || (countryZone.equals(""))) {

        } else {
            lambdaQueryWrapper.eq(CountryZone::getCountryGroup, countryZone);
        }
        if (status.equals("All") || (status.equals(""))) {

        } else {
            lambdaQueryWrapper.eq(CountryZone::getStatus, status);
        }

        Page<CountryZone> pageParam = new Page<>(page, size, true);
        IPage<CountryZone> countryZoneIPage = countryZoneMapper.selectPage(pageParam, lambdaQueryWrapper);
        List<CountryZoneDto> countryZoneDtoList = new ArrayList<>();
        for (CountryZone record : countryZoneIPage.getRecords()) {
            CountryZoneDto countryZoneDto = new CountryZoneDto();
            BeanUtils.copyProperties(record, countryZoneDto);
            countryZoneDtoList.add(countryZoneDto);
        }

        IPage<CountryZoneDto> countryZoneDtoIPage = new Page<>(countryZoneIPage.getCurrent(), countryZoneIPage.getSize(), countryZoneIPage.getTotal());
        countryZoneDtoIPage.setRecords(countryZoneDtoList);

        return new MvneCrmResponse().success().data(countryZoneDtoIPage);
    }

    /**
     * 内部方法
     * @param zone
     * @return
     */
    private Integer searchCountryZone(String zone) {
        LambdaQueryWrapper<SysRoamZoneGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoamZoneGroup::getCountryGroup, zone);
        List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupMapper.selectList(queryWrapper);
        if ((sysRoamZoneGroupList.size()!=0)) {
            // 相同地区组的groupid是一样的
            return sysRoamZoneGroupList.get(0).getGroupId();
        } else {
            return null;
        }
    }

    @Cacheable(value = "cm_user_detail", key = "#p0", cacheManager = "localRedisCacheManager")
    public CmUserDetail selectCmUserDetail(Long userId) {
        CmUserDetail cmUserDetail = cmUserDetailMapper.selectById(userId);
        System.out.println(cmUserDetail);
        return cmUserDetail;
    }

}
