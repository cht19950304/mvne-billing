package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.preparation.entity.BdOperatorCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BdOperatorCodeMapper extends BaseMapper<BdOperatorCode> {
    int deleteByPrimaryKey(Long id);

//    int insert(BdOperatorCode record);

    int insertSelective(BdOperatorCode record);

    BdOperatorCode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BdOperatorCode record);

    int updateByPrimaryKey(BdOperatorCode record);
}