package com.cmit.mvne.billing.preparation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.preparation.dao.CdrErrorFileMapper;
import com.cmit.mvne.billing.preparation.entity.CdrErrorFile;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import com.cmit.mvne.billing.preparation.service.CdrErrorFileService;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/19
 */
@Service
public class CdrErrorFileServiceImpl extends ServiceImpl<CdrErrorFileMapper, CdrErrorFile> implements CdrErrorFileService {
    @Override
    public IPage<RejectedDTO> findRejectedFilesPage(String errorType, String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable) {

        Page<RejectedDTO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize(), true);
        List<RejectedDTO> rejectedDTOList = this.baseMapper.selectByRejectedRequestDTOPage(page, errorType, filename, errorCode, status,
                startTime!=null? DateTimeUtil.getDateofTimestamp(startTime):null, endTime!=null?DateTimeUtil.getDateofTimestamp(endTime):null);
        page.setRecords(rejectedDTOList);
        return page;
    }

    @Override
    public List<RejectedDTO> findRejectedFiles(String errorType, String filename, String errorCode, String status, Long startTime, Long endTime) {
        return this.baseMapper.selectByRejectedRequestDTO(errorType, filename, errorCode, status,
                startTime!=null? DateTimeUtil.getDateofTimestamp(startTime):null, endTime!=null?DateTimeUtil.getDateofTimestamp(endTime):null);
    }
}
