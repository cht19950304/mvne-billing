package com.cmit.mvne.billing.user.analysis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsReratDto;
import com.cmit.mvne.billing.user.analysis.entity.RatingCdrGprsRerat;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-06-03
 */
public interface RatingCdrGprsReratService extends IService<RatingCdrGprsRerat> {
    List<RatingCdrGprsRerat> reratSelect(@Param("sqlSegment") String sqlSegment);

    IPage<QueryCdrGprsReratDto> reratQueryRerat(IPage<QueryCdrGprsReratDto> page, QueryWrapper<QueryCdrGprsReratDto> queryWrapper);
}
