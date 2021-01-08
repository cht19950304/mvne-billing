package com.cmit.mvne.billing.rating.gprs.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/3/5 14:40
 */

@Data
@Component
@ConfigurationProperties(prefix = "yellow-mobile")
public class SystemConfiguration {

    private BigDecimal limitedValue;

    private BigDecimal limitedFee;

    private Integer ratingRedoExpireDays;
}
