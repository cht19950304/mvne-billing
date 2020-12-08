package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.preparation.entity.CdrGprsRating;

public interface CdrGprsRatingMapper extends BaseMapper<CdrGprsRating> {
    int deleteByPrimaryKey(Long id);

    int insert(CdrGprsRating record);

    int insertSelective(CdrGprsRating record);

    CdrGprsRating selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrGprsRating record);

    int updateByPrimaryKey(CdrGprsRating record);
}