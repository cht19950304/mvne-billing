package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.PmProductDup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2020-07-28
 */
public interface PmProductDupMapper extends BaseMapper<PmProductDup> {
    public BigDecimal selectByProductId(Long productId);
}
