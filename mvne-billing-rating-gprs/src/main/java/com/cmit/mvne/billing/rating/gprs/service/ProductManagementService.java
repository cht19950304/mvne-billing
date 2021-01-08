package com.cmit.mvne.billing.rating.gprs.service;

import com.cmit.mvne.billing.rating.gprs.common.MvneCrmResponse;
import com.cmit.mvne.billing.rating.gprs.dto.ProductInfo;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/23 11:19
 */
public interface ProductManagementService {
    MvneCrmResponse createProduct(ProductInfo productInfo);

    MvneCrmResponse expireProduct(Long productId, Long expireDate);

    MvneCrmResponse searchProduct(String productName, String productStatus, String regionCod, int page, int size);

    MvneCrmResponse searchCountryZone(Boolean isCreatingProduct);

    MvneCrmResponse searchCountry(String countryZone, String status, int page, int size);
}
