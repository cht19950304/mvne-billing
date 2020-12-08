package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumMonth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SettleSiminnSumMonthMapper extends BaseMapper<SettleSiminnSumMonth> {
    int deleteByPrimaryKey(Long id);

//    int insert(SettleSiminnSumMonth record);

    int insertSelective(SettleSiminnSumMonth record);

    SettleSiminnSumMonth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SettleSiminnSumMonth record);

    int updateByPrimaryKey(SettleSiminnSumMonth record);

    List<SettleSiminnSumMonth> selectByInvoicingPeriod(String invoicingPeriod);
}