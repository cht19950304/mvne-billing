package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.CountryOperator;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.user.analysis.entity.PmProduct;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-24
 */
public interface CountryOperatorService extends IService<CountryOperator> {

    CountryOperator cache(CountryOperator countryOperator);

    String cacheName(CountryOperator countryOperator);

    CountryOperator selectByOperator(String operator);

    String selectByOperCode(String operatorCode);
}
