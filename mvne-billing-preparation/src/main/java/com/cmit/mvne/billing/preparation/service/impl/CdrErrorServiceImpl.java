package com.cmit.mvne.billing.preparation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.preparation.dao.CdrErrorMapper;
import com.cmit.mvne.billing.preparation.entity.CdrError;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import com.cmit.mvne.billing.preparation.service.CdrErrorService;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/28
 */
@Service
public class CdrErrorServiceImpl extends ServiceImpl<CdrErrorMapper, CdrError> implements CdrErrorService {

    @Override
    public IPage<RejectedDTO> findRejectedCdrsPage(String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable) {
        Page<RejectedDTO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize(), true);

        List<RejectedDTO> rejectedDTOList = this.baseMapper.selectByRejectedRequestDTOPage(page, filename, errorCode, status,
                startTime!=null?DateTimeUtil.getDateofTimestamp(startTime):null, endTime!=null?DateTimeUtil.getDateofTimestamp(endTime):null);

        page.setRecords(rejectedDTOList);
        return page;
    }

    @Override
    public List<RejectedDTO> findRejectedCdrs(String filename, String errorCode, String status, Long startTime, Long endTime) {

        List<RejectedDTO> rejectedDTOList = this.baseMapper.selectByRejectedRequestDTO(filename, errorCode, status,
                startTime!=null?DateTimeUtil.getDateofTimestamp(startTime):null, endTime!=null?DateTimeUtil.getDateofTimestamp(endTime):null);

        return rejectedDTOList;
    }
}
