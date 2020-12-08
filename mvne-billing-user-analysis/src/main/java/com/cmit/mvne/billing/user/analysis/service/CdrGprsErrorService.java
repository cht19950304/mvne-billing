package com.cmit.mvne.billing.user.analysis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsErrDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprsError;
import com.cmit.mvne.billing.user.analysis.entity.QueryUserCdrInfoDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/2
 */
@Service
public interface CdrGprsErrorService extends IService<CdrGprsError> {
    List<CdrGprsError> errRedoSelect(String sqlSegment);

    IPage<QueryCdrGprsErrDto> redoQuery(IPage<QueryCdrGprsErrDto> page, QueryWrapper<QueryCdrGprsErrDto> queryWrapper);
}
