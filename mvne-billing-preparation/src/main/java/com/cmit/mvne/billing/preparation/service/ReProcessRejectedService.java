package com.cmit.mvne.billing.preparation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/29
 */
public interface ReProcessRejectedService {

    IPage<RejectedDTO> getRejectedCdrsPage(String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable);

    IPage<RejectedDTO> getRejectedFilesPage(String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable) throws IOException;

    void reprocessRejectedCdrs(List<Long> idList);

    List<RejectedDTO> getRejectedCdrs(String filename, String errorCode, String status, Long startTime, Long endTime);

    List<RejectedDTO> getRejectedFiles(String filename, String errorCode, String status, Long startTime, Long endTime);
}
