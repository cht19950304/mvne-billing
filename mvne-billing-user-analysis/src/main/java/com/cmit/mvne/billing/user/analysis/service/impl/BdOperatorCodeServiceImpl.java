package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmit.mvne.billing.user.analysis.entity.BdOperatorCode;
import com.cmit.mvne.billing.user.analysis.mapper.BdOperatorCodeMapper;
import com.cmit.mvne.billing.user.analysis.service.BdOperatorCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-06-15
 */
@Service
public class BdOperatorCodeServiceImpl extends ServiceImpl<BdOperatorCodeMapper, BdOperatorCode> implements BdOperatorCodeService {
    @Autowired
    BdOperatorCodeMapper bdOperatorCodeMapper;

    @Override
    @CachePut(value = "BdOperatorCode", key = "#p0.operatorCode", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public BdOperatorCode cache(BdOperatorCode bdOperatorCode) {
        return bdOperatorCode;
    }

    @Override
    @Cacheable(value = "BdOperatorCode", key = "#p0", unless="#result == null", cacheManager = "selectRedisCacheManager")
    public BdOperatorCode selectByOperatorCode(String operatorCode) {
        LambdaQueryWrapper<BdOperatorCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BdOperatorCode::getOperatorCode, operatorCode);
        List<BdOperatorCode> bdOperatorCodeList = bdOperatorCodeMapper.selectList(lambdaQueryWrapper);

        if (bdOperatorCodeList.size()!=0) {
            return bdOperatorCodeList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String selectByOperCodeInfo(String operatorCode) {
        return bdOperatorCodeMapper.selectByOperCodeInfo(operatorCode);
    }
}
