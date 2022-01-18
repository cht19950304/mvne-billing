package com.cmit.mvne.billing.rating.gprs.controller;

import com.cmit.mvne.billing.rating.gprs.common.Measures;
import com.cmit.mvne.billing.rating.gprs.remote.Resource;
import com.cmit.mvne.billing.rating.gprs.service.ProductManagementService;
import com.cmit.mvne.billing.rating.gprs.common.MvneCrmResponse;
import com.cmit.mvne.billing.rating.gprs.dto.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/23 10:53
 */
@RestController
@RequestMapping("/productManagement")
public class ProductManagementController {
    @Autowired
    ProductManagementService productManagementService;

    @RequestMapping(value = "/createProduct", method = RequestMethod.POST)
    MvneCrmResponse createProduct(@RequestBody @Valid ProductInfo productInfo) {

        /*ProductInfo productInfo = new ProductInfo("test");
        productInfo.setDesc("test");
        productInfo.setEffectiveDate(new Date());
        productInfo.setEffectiveTime(new Integer(13));
        productInfo.setExpireDate(new Date());
        productInfo.setGroupId(1);
        productInfo.setProductFee(new BigDecimal(3000));
        productInfo.setProductName("nametest");
        productInfo.setRegionCode("A");
        Resource overFee = new Resource(new BigDecimal(0.5), new BigDecimal(10301), new BigDecimal(1), new BigDecimal(105));
        List<Resource> overFeeList = new ArrayList<>();
        overFeeList.add(overFee);
        productInfo.setOverFee(overFeeList);
        Resource resource = new Resource(new BigDecimal("3072"), new BigDecimal(105));
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(resource);
        productInfo.setResource(resourceList);*/

        BigDecimal min = new BigDecimal(0);
        Resource resource = productInfo.getResource().get(0);
        Resource overFee = productInfo.getOverFee().get(0);

        BigDecimal valueAmountFree_res_front = new BigDecimal(String.valueOf(resource.getResourceMap().get("valueAmount")));
        BigDecimal feeAmount_front = new BigDecimal(String.valueOf(overFee.getResourceMap().get("feeAmount")));
        BigDecimal valueAmount = new BigDecimal(String.valueOf(overFee.getResourceMap().get("valueAmount")));
        MvneCrmResponse mvneCrmResponse = new MvneCrmResponse().fail();
        if ((valueAmountFree_res_front.compareTo(min)<=-1) ||
                (feeAmount_front.compareTo(min)<=-1) ||
                (valueAmount.compareTo(min)<=-1)) {
            mvneCrmResponse = mvneCrmResponse.message("FeeAmount or valueAmount is less than 0.");
        } else if (Measures.MB.compareTo(overFee.getResourceMap().get("valueMeasure"))!=0){
            // 默认套外为MB为单位
            mvneCrmResponse = mvneCrmResponse.message("OverFee should be MB.");
        } else if (productInfo.getEffectiveDate().compareTo(productInfo.getExpireDate()) > -1) {
            mvneCrmResponse = mvneCrmResponse.message("Expire date should be later than effective date.");
        } else
        {
            mvneCrmResponse = productManagementService.createProduct(productInfo);
        }
        return mvneCrmResponse;
    }

    @RequestMapping(value = "/getLong", method = RequestMethod.POST)
    Long getLong() {
        Date date = new Date();
        return date.getTime();
    }

    @RequestMapping(value = "/expireProduct", method = RequestMethod.POST)
    MvneCrmResponse expireProduct(@RequestParam(value="productId") Long productId,
                                  @RequestParam(value="expireDate") Long expireDate) {
        MvneCrmResponse mvneCrmResponse = productManagementService.expireProduct(productId, expireDate);
        return mvneCrmResponse;
    }

    @RequestMapping(value = "/searchProduct", method = RequestMethod.POST)
    MvneCrmResponse searchProduct(@RequestParam(value="productName") String productName,
                                  @RequestParam(value="productStatus") String productStatus,
                                  @RequestParam(value="regionCode") String regionCode,
                                  @RequestParam(value="page") int page, @RequestParam(value="size") int size) {
        // 4已生效，6已下架，1未生效，0全部
        MvneCrmResponse mvneCrmResponse = productManagementService.searchProduct(productName, productStatus, regionCode, page, size);
        return mvneCrmResponse;
    }

    /**
     * 创建产品时查询的分区，只会查询有效分区
     * @return
     */
    @RequestMapping(value = "/searchCountryZoneWhenCreateProduct", method = RequestMethod.POST)
    MvneCrmResponse searchCountryZoneWhenCreateProduct() {
        return productManagementService.searchCountryZone(true);
    }

    @RequestMapping(value = "/searchCountryZone", method = RequestMethod.POST)
    MvneCrmResponse searchCountryZone() {
        return productManagementService.searchCountryZone(false);
    }

    @RequestMapping(value = "/searchCountry", method = RequestMethod.POST)
    MvneCrmResponse searchCountry(@RequestParam(value="countryZone") String countryZone,
                                    @RequestParam(value="status") String status,
                                  @RequestParam(value = "page") int page,
                                  @RequestParam(value = "size") int size) {
        return productManagementService.searchCountry(countryZone, status, page, size);
    }
}
