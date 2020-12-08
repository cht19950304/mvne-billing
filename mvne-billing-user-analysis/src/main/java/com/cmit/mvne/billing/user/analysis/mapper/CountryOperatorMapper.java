package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.CountryOperator;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2020-02-24
 */
public interface CountryOperatorMapper extends BaseMapper<CountryOperator> {
    String selectByOperCode(String operatorCode);
}
