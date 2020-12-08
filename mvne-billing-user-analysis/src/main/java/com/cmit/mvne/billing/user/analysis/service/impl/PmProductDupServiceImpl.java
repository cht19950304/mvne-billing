package com.cmit.mvne.billing.user.analysis.service.impl;

import com.cmit.mvne.billing.user.analysis.entity.PmProductDup;
import com.cmit.mvne.billing.user.analysis.mapper.PmProductDupMapper;
import com.cmit.mvne.billing.user.analysis.service.PmProductDupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-07-28
 */
@Service
public class PmProductDupServiceImpl extends ServiceImpl<PmProductDupMapper, PmProductDup> implements PmProductDupService {
    @Autowired
    PmProductDupMapper pmProductDupMapper;

    @Override
    public BigDecimal selectByProductId(Long productId) {
        return pmProductDupMapper.selectByProductId(productId);
    }
}
