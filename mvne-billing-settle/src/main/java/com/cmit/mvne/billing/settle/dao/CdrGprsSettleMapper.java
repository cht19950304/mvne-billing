package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.CdrGprsSettle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CdrGprsSettleMapper extends BaseMapper<CdrGprsSettle> {
    int deleteByPrimaryKey(Long id);

//    int insert(CdrGprsSettle record);

    int insertSelective(CdrGprsSettle record);

    CdrGprsSettle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrGprsSettle record);

    int updateByPrimaryKey(CdrGprsSettle record);
}