package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.preparation.entity.CdrGprsSettle;

public interface CdrGprsSettleMapper extends BaseMapper<CdrGprsSettle> {
    int deleteByPrimaryKey(Long id);

    int insert(CdrGprsSettle record);

    int insertSelective(CdrGprsSettle record);

    CdrGprsSettle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrGprsSettle record);

    int updateByPrimaryKey(CdrGprsSettle record);
}