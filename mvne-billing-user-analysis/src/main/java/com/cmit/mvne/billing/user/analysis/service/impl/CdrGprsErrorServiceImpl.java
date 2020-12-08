package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsErrDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprsError;
import com.cmit.mvne.billing.user.analysis.mapper.CdrGprsErrorMapper;
import com.cmit.mvne.billing.user.analysis.service.CdrGprsErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/2
 */
@Service
public class CdrGprsErrorServiceImpl extends ServiceImpl<CdrGprsErrorMapper, CdrGprsError> implements CdrGprsErrorService {
    @Autowired
    CdrGprsErrorMapper cdrGprsErrorMapper;

    @Override
    public List<CdrGprsError> errRedoSelect(String sqlSegment) {
        return cdrGprsErrorMapper.errSelect(sqlSegment);
    }

    @Override
    public IPage<QueryCdrGprsErrDto> redoQuery(IPage<QueryCdrGprsErrDto> page, QueryWrapper<QueryCdrGprsErrDto> queryWrapper) {
        return cdrGprsErrorMapper.redoQuery(page, queryWrapper);
    }
}
