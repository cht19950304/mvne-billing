package com.cmit.mvne.billing.user.analysis.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsErrDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprsError;
import com.cmit.mvne.billing.user.analysis.entity.ErrCdrGprs;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/2
 */
public interface CdrGprsErrorMapper extends BaseMapper<CdrGprsError> {
    List<CdrGprsError> errSelect(@Param("sqlSegment") String sqlSegment);

    IPage<QueryCdrGprsErrDto> redoQuery(IPage<QueryCdrGprsErrDto> page, @Param(Constants.WRAPPER) QueryWrapper<QueryCdrGprsErrDto> queryWrapper);
}
