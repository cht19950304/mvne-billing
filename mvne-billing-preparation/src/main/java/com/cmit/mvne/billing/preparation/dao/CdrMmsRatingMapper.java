package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.preparation.entity.CdrMmsRating;

public interface CdrMmsRatingMapper extends BaseMapper<CdrMmsRating> {
    int deleteByPrimaryKey(Long id);

    int insert(CdrMmsRating record);

    int insertSelective(CdrMmsRating record);

    CdrMmsRating selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrMmsRating record);

    int updateByPrimaryKey(CdrMmsRating record);
}