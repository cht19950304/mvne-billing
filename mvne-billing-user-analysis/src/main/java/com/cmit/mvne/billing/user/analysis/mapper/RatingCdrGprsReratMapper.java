package com.cmit.mvne.billing.user.analysis.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsDto;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsReratDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.RatingCdrGprsRerat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2020-06-03
 */
public interface RatingCdrGprsReratMapper extends BaseMapper<RatingCdrGprsRerat> {
    List<RatingCdrGprsRerat> reratSelect(@Param("sqlSegment") String sqlSegment);

    IPage<QueryCdrGprsReratDto> reratQueryRerat(IPage<QueryCdrGprsReratDto> page, @Param(Constants.WRAPPER) QueryWrapper<QueryCdrGprsReratDto> queryWrapper);
}
