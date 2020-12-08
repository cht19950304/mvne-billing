package com.cmit.mvne.billing.settle.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.settle.dao.SettleCdrSmsErrorMapper;
import com.cmit.mvne.billing.settle.entity.SettleCdrSmsError;
import com.cmit.mvne.billing.settle.entity.dto.SettleCdrSmsErrorDTO;
import com.cmit.mvne.billing.settle.service.SettleCdrSmsErrorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/8
 */
@Service
public class SettleCdrSmsErrorServiceImpl extends ServiceImpl<SettleCdrSmsErrorMapper, SettleCdrSmsError> implements SettleCdrSmsErrorService {
    @Override
    public List<SettleCdrSmsErrorDTO> selectSettleCdrSmsErrorByCondition(Page<SettleCdrSmsErrorDTO> page, String filename, String status, String msisdn, String errorCode, Long startTime, Long endTime) {
        return getBaseMapper().selectSettleCdrSmsErrorByCondition(page, filename, status, msisdn, errorCode, startTime, endTime);
    }
}
