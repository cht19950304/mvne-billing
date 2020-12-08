package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.RatingRate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
public interface RatingRateService extends IService<RatingRate> {

    RatingRate cacheProductIdAndItemId(RatingRate ratingRate);

    List<RatingRate> cacheProductId(Long productId, List<RatingRate> ratingRate);

    RatingRate selectByKey(Long productId, Long itemId);

    List<RatingRate> selectByProductId(Long productId);
}
