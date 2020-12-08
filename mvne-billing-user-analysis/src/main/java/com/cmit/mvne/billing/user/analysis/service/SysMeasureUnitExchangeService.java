package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.SysMeasureUnitExchange;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
public interface SysMeasureUnitExchangeService extends IService<SysMeasureUnitExchange> {

    SysMeasureUnitExchange cache(SysMeasureUnitExchange sysMeasureUnitExchange);

    public SysMeasureUnitExchange exchange(BigDecimal measureId, BigDecimal destMeasureId);
}
