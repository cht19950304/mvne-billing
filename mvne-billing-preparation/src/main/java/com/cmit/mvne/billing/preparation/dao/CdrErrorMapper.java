package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.preparation.entity.CdrError;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CdrErrorMapper extends BaseMapper<CdrError> {
    int deleteByPrimaryKey(Long id);

//    int insert(CdrError record);

    int insertSelective(CdrError record);

    CdrError selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrError record);

    int updateByPrimaryKeyWithBLOBs(CdrError record);

    int updateByPrimaryKey(CdrError record);

    List<RejectedDTO> selectByRejectedRequestDTOPage(Page<RejectedDTO> page, @Param("filename") String filename, @Param("errorCode") String errorCode, @Param("status") String status, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<RejectedDTO> selectByRejectedRequestDTO(@Param("filename") String filename, @Param("errorCode") String errorCode, @Param("status") String status,  @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}