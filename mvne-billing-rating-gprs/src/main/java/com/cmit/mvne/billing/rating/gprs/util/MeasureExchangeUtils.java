package com.cmit.mvne.billing.rating.gprs.util;

import com.cmit.mvne.billing.user.analysis.entity.SysMeasureUnitExchange;
import com.cmit.mvne.billing.user.analysis.service.SysMeasureUnitExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/26 21:00
 */
@Component
public class MeasureExchangeUtils {
    private static SysMeasureUnitExchangeService mySysMeasureUnitExchangeService;

    @Autowired
    SysMeasureUnitExchangeService sysMeasureUnitExchangeService;

    @PostConstruct
    public void init() {
        mySysMeasureUnitExchangeService = sysMeasureUnitExchangeService;
    }

    public static BigDecimal exchange(BigDecimal value, BigDecimal measureId, BigDecimal destMeasureId, int scale) {
        SysMeasureUnitExchange exchange = mySysMeasureUnitExchangeService.exchange(measureId, destMeasureId);
        BigDecimal destValue = value.multiply(exchange.getExchangeNumerator())
                .divide(exchange.getExchangeDenominator(), scale, RoundingMode.UNNECESSARY);

        return destValue;
    }

    public static BigDecimal exchangeFloor(BigDecimal value, BigDecimal measureId, BigDecimal destMeasureId, int scale) {
        SysMeasureUnitExchange exchange = mySysMeasureUnitExchangeService.exchange(measureId, destMeasureId);
        BigDecimal destValue = value.multiply(exchange.getExchangeNumerator())
                .divide(exchange.getExchangeDenominator(), scale, RoundingMode.FLOOR);

        return destValue;
    }

    public static BigDecimal exchangeCeiling(BigDecimal value, BigDecimal measureId, BigDecimal destMeasureId, int scale) {
        SysMeasureUnitExchange exchange = mySysMeasureUnitExchangeService.exchange(measureId, destMeasureId);
        BigDecimal destValue = value.multiply(exchange.getExchangeNumerator())
                .divide(exchange.getExchangeDenominator(), scale, RoundingMode.CEILING);

        return destValue;
    }
}
