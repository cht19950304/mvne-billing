/*
package com.cmit.mvne.billing.user.analysis.util;

import org.springframework.stereotype.Component;

import com.cmsz.mvne.billing.user.analysis.pojo.PricePlan;
import com.cmsz.mvne.billing.user.analysis.pojo.ProductPricePlan;

@Component
public class ReturnObjectUtil {
	public ProductPricePlan returnObject(Integer priceType, Integer priceId, ProductPricePlan productPricePlan) {
		if(null != priceType) {
			if(priceType.equals(0)) {
				productPricePlan.setBasicCharge(new PricePlan(priceId, priceType));
			}else if(priceType.equals(1)) {
				productPricePlan.setAccumulativeDiscount(new PricePlan(priceId, priceType));
			}else if(priceType.equals(2)) {
				productPricePlan.setAccumulativeGift(new PricePlan(priceId, priceType));
			}else if(priceType.equals(3)) {
				productPricePlan.setPeriodicalFreeRes(new PricePlan(priceId, priceType));
			}else if(priceType.equals(4)) {
				productPricePlan.setDisposableFreeRes(new PricePlan(priceId, priceType));
			}else if(priceType.equals(5)) {
				productPricePlan.setDiscountOverDiscount(new PricePlan(priceId, priceType));
			}else if(priceType.equals(6)) {
				productPricePlan.setReturnTax(new PricePlan(priceId, priceType));
			}else if(priceType.equals(7)) {
				productPricePlan.setFixedCharge(new PricePlan(priceId, priceType));
			}else if(priceType.equals(8)) {
				productPricePlan.setAccountDiscount(new PricePlan(priceId, priceType));
			}else if(priceType.equals(9)) {
				productPricePlan.setDisposableFee(new PricePlan(priceId, priceType));
			}
		}
		return productPricePlan;
	}
}
*/
