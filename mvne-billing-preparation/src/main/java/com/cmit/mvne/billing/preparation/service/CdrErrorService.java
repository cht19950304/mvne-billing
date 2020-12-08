package com.cmit.mvne.billing.preparation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.preparation.entity.CdrError;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/28
 */
public interface CdrErrorService extends IService<CdrError> {
    IPage<RejectedDTO> findRejectedCdrsPage(String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable);

    List<RejectedDTO> findRejectedCdrs(String filename, String errorCode, String status, Long startTime, Long endTime);
}
