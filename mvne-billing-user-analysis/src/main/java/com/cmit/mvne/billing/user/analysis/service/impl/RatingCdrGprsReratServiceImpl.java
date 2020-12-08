package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsReratDto;
import com.cmit.mvne.billing.user.analysis.entity.RatingCdrGprsRerat;
import com.cmit.mvne.billing.user.analysis.mapper.RatingCdrGprsReratMapper;
import com.cmit.mvne.billing.user.analysis.service.RatingCdrGprsReratService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-06-03
 */
@Service
public class RatingCdrGprsReratServiceImpl extends ServiceImpl<RatingCdrGprsReratMapper, RatingCdrGprsRerat> implements RatingCdrGprsReratService {
    @Autowired
    RatingCdrGprsReratMapper ratingCdrGprsReratMapper;

    @Override
    public List<RatingCdrGprsRerat> reratSelect(String sqlSegment) {
        return ratingCdrGprsReratMapper.reratSelect(sqlSegment);
    }

    @Override
    public IPage<QueryCdrGprsReratDto> reratQueryRerat(IPage<QueryCdrGprsReratDto> page, QueryWrapper<QueryCdrGprsReratDto> queryWrapper) {
        return ratingCdrGprsReratMapper.reratQueryRerat(page, queryWrapper);
    }
}
