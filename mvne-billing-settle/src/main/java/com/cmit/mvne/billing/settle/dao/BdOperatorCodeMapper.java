package com.cmit.mvne.billing.settle.dao;

import com.cmit.mvne.billing.settle.entity.BdOperatorCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BdOperatorCodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BdOperatorCode record);

    int insertSelective(BdOperatorCode record);

    BdOperatorCode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BdOperatorCode record);

    int updateByPrimaryKey(BdOperatorCode record);
}