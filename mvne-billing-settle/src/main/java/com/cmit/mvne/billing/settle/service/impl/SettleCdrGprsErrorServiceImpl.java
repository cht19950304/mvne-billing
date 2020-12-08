package com.cmit.mvne.billing.settle.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.settle.dao.SettleCdrGprsErrorMapper;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprsError;
import com.cmit.mvne.billing.settle.entity.dto.SettleCdrGprsErrorDTO;
import com.cmit.mvne.billing.settle.service.SettleCdrGprsErrorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
@Service
public class SettleCdrGprsErrorServiceImpl extends ServiceImpl<SettleCdrGprsErrorMapper, SettleCdrGprsError> implements SettleCdrGprsErrorService {
    @Override
    public List<SettleCdrGprsErrorDTO> selectSettleCdrGprsErrorByCondition(Page<SettleCdrGprsErrorDTO> page, String filename, String status, String msisdn, String errorCode, Long startTime, Long endTime) {
        return getBaseMapper().selectSettleCdrGprsErrorByCondition(page, filename, status, msisdn, errorCode, startTime, endTime);
    }
}
