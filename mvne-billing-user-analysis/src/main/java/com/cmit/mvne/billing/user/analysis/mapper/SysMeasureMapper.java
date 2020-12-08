package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.SysMeasure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * <p>
 * Definition table of global measurement, including currency a Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Mapper
public interface SysMeasureMapper extends BaseMapper<SysMeasure> {
}
