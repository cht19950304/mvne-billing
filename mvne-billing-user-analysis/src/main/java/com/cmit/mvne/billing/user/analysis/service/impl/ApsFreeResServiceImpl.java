package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cmit.mvne.billing.user.analysis.entity.ApsFreeRes;
import com.cmit.mvne.billing.user.analysis.mapper.ApsFreeResMapper;
import com.cmit.mvne.billing.user.analysis.service.ApsFreeResService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
@Service
public class ApsFreeResServiceImpl extends ServiceImpl<ApsFreeResMapper, ApsFreeRes> implements ApsFreeResService {

    @Autowired
    private ApsFreeResMapper apsFreeResMapper;

    @Override
    public List<ApsFreeRes> findByUserId(Long userId) {
        return apsFreeResMapper.findByUserId(userId);
    }

    @Override
    public List<ApsFreeRes> findByUserIdProdIns(Long userId, Long productInsId) {
        return apsFreeResMapper.findByUserIdProdIns(userId,productInsId);
    }

    @Override
    public void insert(ApsFreeRes apsFreeRes) {
        apsFreeResMapper.insert(apsFreeRes);
    }

    @Override
    public int updateDeleteFreeRes(Long userId, Date expireDate) {
        return apsFreeResMapper.updateDeleteFreeRes(userId, expireDate);
    }

    @Override
    public int updateFreeRes(Long userId, Long productInstId, Long itemId, BigDecimal usedValue) {
        LambdaUpdateWrapper<ApsFreeRes> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ApsFreeRes::getUserId, userId)
                .eq(ApsFreeRes::getProductInsId, productInstId)
                .eq(ApsFreeRes::getItemId, itemId)
                .set(ApsFreeRes::getUsedValue, usedValue);
        return apsFreeResMapper.update(null, lambdaUpdateWrapper);
    }



    @Override
    public List<ApsFreeRes> selectFreeRes(Date startTime, Date endTime) {
        return apsFreeResMapper.selectFreeRes(startTime,endTime);
    }

    @Override
    public ApsFreeRes selectByKey(Long userId, Long productInsId, Long itemId) {
        LambdaQueryWrapper<ApsFreeRes> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ApsFreeRes::getUserId, userId)
                .eq(ApsFreeRes::getProductInsId, productInsId)
                .eq(ApsFreeRes::getItemId, itemId);
        return apsFreeResMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<ApsFreeRes> findValidFreeRes(Long userId) {
        return apsFreeResMapper.findValidFreeRes(userId);
    }

}
