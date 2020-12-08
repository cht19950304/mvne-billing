package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.PmProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
public interface PmProductService extends IService<PmProduct> {

    PmProduct cache(PmProduct pmProduct);

    String cacheName(PmProduct pmProduct);

    Long create(PmProduct pmProduct);

    PmProduct select(Long productId);

    String selectByProductId(Long productId);
}
