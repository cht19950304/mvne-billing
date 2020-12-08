package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.CdrMmsSettle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CdrMmsSettleMapper extends BaseMapper<CdrMmsSettle> {
    int deleteByPrimaryKey(Long id);

//    int insert(CdrMmsSettle record);

    int insertSelective(CdrMmsSettle record);

    CdrMmsSettle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrMmsSettle record);

    int updateByPrimaryKey(CdrMmsSettle record);
}