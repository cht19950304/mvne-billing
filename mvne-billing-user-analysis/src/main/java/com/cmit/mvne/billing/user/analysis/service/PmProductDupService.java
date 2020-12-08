package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.PmProductDup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-07-28
 */
public interface PmProductDupService extends IService<PmProductDup> {
    public BigDecimal selectByProductId(Long productId);
}
