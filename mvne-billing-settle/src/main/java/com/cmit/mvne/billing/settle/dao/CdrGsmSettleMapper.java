package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.CdrGsmSettle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CdrGsmSettleMapper extends BaseMapper<CdrGsmSettle> {
    int deleteByPrimaryKey(Long id);

//    int insert(CdrGsmSettle record);

    int insertSelective(CdrGsmSettle record);

    CdrGsmSettle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrGsmSettle record);

    int updateByPrimaryKey(CdrGsmSettle record);
}