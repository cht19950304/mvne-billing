package com.cmit.mvne.billing.preparation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.preparation.entity.CdrErrorFile;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface CdrErrorFileMapper extends BaseMapper<CdrErrorFile> {
    int deleteByPrimaryKey(Long id);

//    int insert(CdrErrorFile record);

    int insertSelective(CdrErrorFile record);

    CdrErrorFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CdrErrorFile record);

    int updateByPrimaryKey(CdrErrorFile record);

    List<RejectedDTO> selectByRejectedRequestDTOPage(Page<RejectedDTO> page, String errorType, String filename, String errorCode, String status, Date startTime, Date endTime);

    List<RejectedDTO> selectByRejectedRequestDTO(String errorType, String filename, String errorCode, String status, Date startTime, Date endTime);

}