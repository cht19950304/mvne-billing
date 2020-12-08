package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.settle.entity.SettleCdrSmsError;
import com.cmit.mvne.billing.settle.entity.dto.SettleCdrSmsErrorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SettleCdrSmsErrorMapper extends BaseMapper<SettleCdrSmsError> {
    int deleteByPrimaryKey(Long id);

//    int insert(SettleCdrSmsError record);

    int insertSelective(SettleCdrSmsError record);

    SettleCdrSmsError selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SettleCdrSmsError record);

    int updateByPrimaryKeyWithBLOBs(SettleCdrSmsError record);

    int updateByPrimaryKey(SettleCdrSmsError record);

    List<SettleCdrSmsErrorDTO> selectSettleCdrSmsErrorByCondition(Page<SettleCdrSmsErrorDTO> page, @Param("filename") String filename, @Param("status") String status, @Param("msisdn") String msisdn, @Param("errorCode") String errorCode, @Param("startTime") Long startTime, @Param("endTime") Long endTime);
}