package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.SettleCdrSms;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettleCdrSmsMapper extends BaseMapper<SettleCdrSms> {
    int deleteByPrimaryKey(Long id);

//    int insert(SettleCdrSms record);

    int insertSelective(SettleCdrSms record);

    SettleCdrSms selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SettleCdrSms record);

    int updateByPrimaryKey(SettleCdrSms record);
}