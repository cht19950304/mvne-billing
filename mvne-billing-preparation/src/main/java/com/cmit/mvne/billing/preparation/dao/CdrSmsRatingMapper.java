package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.preparation.entity.CdrSmsRating;

public interface CdrSmsRatingMapper extends BaseMapper<CdrSmsRating> {
    int deleteByPrimaryKey(Long id);

    int insert(CdrSmsRating record);

    int insertSelective(CdrSmsRating record);

    CdrSmsRating selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrSmsRating record);

    int updateByPrimaryKey(CdrSmsRating record);
}