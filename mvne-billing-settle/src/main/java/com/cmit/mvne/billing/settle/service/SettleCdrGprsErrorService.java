package com.cmit.mvne.billing.settle.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprsError;
import com.cmit.mvne.billing.settle.entity.dto.SettleCdrGprsErrorDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/12
 */
public interface SettleCdrGprsErrorService extends IService<SettleCdrGprsError> {
    List<SettleCdrGprsErrorDTO> selectSettleCdrGprsErrorByCondition(Page<SettleCdrGprsErrorDTO> page, String filename, String status, String msisdn, String errorCode, Long startTime, Long endTime);
}
