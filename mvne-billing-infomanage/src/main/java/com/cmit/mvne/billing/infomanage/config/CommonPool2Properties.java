package com.cmit.mvne.billing.infomanage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonPool2Properties {
    @Value("${lettuce.pool.maxTotal}")
    private Integer maxTotal;

    @Value("${lettuce.pool.maxIdle}")
    private Integer maxIdle;

    @Value("${lettuce.pool.minIdle}")
    private Integer minIdle;

    //TODO 其他属性

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }
}
