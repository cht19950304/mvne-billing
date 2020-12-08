package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.preparation.entity.CdrErrorHistory;

public interface CdrErrorHistoryMapper extends BaseMapper<CdrErrorHistory> {
    int deleteByPrimaryKey(Long id);

//    int insert(CdrErrorHistory record);

    int insertSelective(CdrErrorHistory record);

    CdrErrorHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrErrorHistory record);

    int updateByPrimaryKeyWithBLOBs(CdrErrorHistory record);

    int updateByPrimaryKey(CdrErrorHistory record);
}