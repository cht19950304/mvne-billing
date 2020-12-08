package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumDay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SettleSiminnSumDayMapper extends BaseMapper<SettleSiminnSumDay> {
    int deleteByPrimaryKey(Long id);

//    int insert(SettleSiminnSumDay record);

    int insertSelective(SettleSiminnSumDay record);

    SettleSiminnSumDay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SettleSiminnSumDay record);

    int updateByPrimaryKey(SettleSiminnSumDay record);

    List<SettleSiminnSumDay> selectByBillDay(String billDay);
}