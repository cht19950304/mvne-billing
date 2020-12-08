package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.CdrSmsSettle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CdrSmsSettleMapper extends BaseMapper<CdrSmsSettle> {
    int deleteByPrimaryKey(Long id);

//    int insert(CdrSmsSettle record);

    int insertSelective(CdrSmsSettle record);

    CdrSmsSettle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrSmsSettle record);

    int updateByPrimaryKey(CdrSmsSettle record);

}