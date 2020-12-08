package com.cmit.mvne.billing.settle.dao;

import com.cmit.mvne.billing.settle.entity.SettleCdrMmsError;

public interface SettleCdrMmsErrorMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SettleCdrMmsError record);

    int insertSelective(SettleCdrMmsError record);

    SettleCdrMmsError selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SettleCdrMmsError record);

    int updateByPrimaryKeyWithBLOBs(SettleCdrMmsError record);

    int updateByPrimaryKey(SettleCdrMmsError record);
}