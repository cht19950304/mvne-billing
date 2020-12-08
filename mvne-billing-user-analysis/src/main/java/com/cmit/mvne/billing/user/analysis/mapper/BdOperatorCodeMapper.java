package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.BdOperatorCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2020-06-15
 */
public interface BdOperatorCodeMapper extends BaseMapper<BdOperatorCode> {
    String selectByOperCodeInfo(String operatorCode);
}
