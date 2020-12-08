package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.preparation.entity.CdrGsmRating;

public interface CdrGsmRatingMapper extends BaseMapper<CdrGsmRating> {
    int deleteByPrimaryKey(Long id);

    int insert(CdrGsmRating record);

    int insertSelective(CdrGsmRating record);

    CdrGsmRating selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrGsmRating record);

    int updateByPrimaryKey(CdrGsmRating record);
}