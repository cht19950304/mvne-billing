package com.cmit.mvne.billing.preparation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.preparation.entity.CdrErrorFile;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/19
 */
public interface CdrErrorFileService extends IService<CdrErrorFile> {
    IPage<RejectedDTO> findRejectedFilesPage(String filename, String errorCode, String status, Long startTime, Long endTime, Pageable pageable);

    List<RejectedDTO> findRejectedFiles(String filename, String errorCode, String status, Long startTime, Long endTime);
}
