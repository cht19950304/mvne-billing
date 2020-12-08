package com.cmit.mvne.billing.settle.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.settle.entity.SettleCdrSmsError;
import com.cmit.mvne.billing.settle.entity.dto.SettleCdrSmsErrorDTO;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/8
 */
public interface SettleCdrSmsErrorService extends IService<SettleCdrSmsError> {
    List<SettleCdrSmsErrorDTO> selectSettleCdrSmsErrorByCondition(Page<SettleCdrSmsErrorDTO> page, String filename, String status, String msisdn, String errorCode, Long startTime, Long endTime);
}
