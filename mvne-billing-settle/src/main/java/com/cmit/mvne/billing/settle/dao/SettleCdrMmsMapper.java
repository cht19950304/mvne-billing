package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.SettleCdrMms;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettleCdrMmsMapper extends BaseMapper<SettleCdrMms> {
    int deleteByPrimaryKey(Long id);

//    int insert(SettleCdrMms record);

    int insertSelective(SettleCdrMms record);

    SettleCdrMms selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SettleCdrMms record);

    int updateByPrimaryKey(SettleCdrMms record);
}