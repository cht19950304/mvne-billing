package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.SettleSiminnRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SettleSiminnRuleMapper extends BaseMapper<SettleSiminnRule> {
    int deleteByPrimaryKey(Long itemCode);

//    int insert(SettleSiminnRule record);

    int insertSelective(SettleSiminnRule record);

    SettleSiminnRule selectByPrimaryKey(Long itemCode);

    int updateByPrimaryKeySelective(SettleSiminnRule record);

    int updateByPrimaryKey(SettleSiminnRule record);

    List<SettleSiminnRule> selectNotExpireByItemName(String itemName);
}