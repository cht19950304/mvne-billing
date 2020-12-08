package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprsError;
import com.cmit.mvne.billing.settle.entity.dto.SettleCdrGprsErrorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SettleCdrGprsErrorMapper extends BaseMapper<SettleCdrGprsError> {
    int deleteByPrimaryKey(Long id);

//    int insert(SettleCdrGprsError record);

    int insertSelective(SettleCdrGprsError record);

    SettleCdrGprsError selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SettleCdrGprsError record);

    int updateByPrimaryKeyWithBLOBs(SettleCdrGprsError record);

    int updateByPrimaryKey(SettleCdrGprsError record);

    List<SettleCdrGprsErrorDTO> selectSettleCdrGprsErrorByCondition(Page<SettleCdrGprsErrorDTO> page, @Param("filename") String filename, @Param("status") String status, @Param("msisdn") String msisdn, @Param("errorCode") String errorCode, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

}