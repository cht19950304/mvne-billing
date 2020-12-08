package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.user.analysis.entity.CmProdInsInfo;
import com.cmit.mvne.billing.user.analysis.mapper.CmProdInsInfoMapper;
import com.cmit.mvne.billing.user.analysis.service.CmProdInsInfoService;
import com.cmit.mvne.billing.user.analysis.service.CmUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
@Service
@Slf4j
public class CmProdInsInfoServiceImpl extends ServiceImpl<CmProdInsInfoMapper, CmProdInsInfo> implements CmProdInsInfoService {
    @Autowired
    CmProdInsInfoMapper cmProdInsInfoMapper;
    @Autowired
    CmProdInsInfoService cmProdInsInfoService;


    @Override
    @Cacheable(value = "cm_prod_ins_info", key = "#root.args[0]", unless="#result == null",cacheManager = "selectRedisCacheManager")
    public List<CmProdInsInfo> findAllByUserId(Long userId) {
        log.info("CmProdInsInfoServiceImpl-findAllByUserId select DB !");
        return cmProdInsInfoMapper.selectByUserId(userId);
    }

    @Override
    @CachePut(value = "cm_prod_ins_info", key = "#root.args[0]", unless="#result == null",cacheManager = "localRedisCacheManager")
    public List<CmProdInsInfo> insertCreate(Long userId,CmProdInsInfo cmProdInsInfo) {
        List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoMapper.findValidProd(userId);
        log.info("CmProdInsInfoServiceImpl-insert findValidProd cmProdInsInfoList is {}",cmProdInsInfoList);
        if ( cmProdInsInfoList.size() > 0 ){
            // 返回空，则说明数据库有生效的产品，有异常
            return null;
        }
        cmProdInsInfoMapper.insert(cmProdInsInfo);
        return cmProdInsInfoMapper.selectByUserId(userId);
    }

    @Override
    @CachePut(value = "cm_prod_ins_info", key = "#root.args[0]", unless="#result == null",cacheManager = "localRedisCacheManager")
    public List<CmProdInsInfo> insertOffer(Long userId,CmProdInsInfo cmProdInsInfo) {
        List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoMapper.findValidProd(userId);
        log.info("CmProdInsInfoServiceImpl-insert findValidProd cmProdInsInfoList is {}",cmProdInsInfoList);
        if ( cmProdInsInfoList.size() > 0 ){
            LambdaUpdateWrapper<CmProdInsInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(CmProdInsInfo::getUserId,userId);
            updateWrapper.gt(CmProdInsInfo::getExpireDate,new Date());
            updateWrapper.set(CmProdInsInfo::getExpireDate,cmProdInsInfo.getValidDate());
            Boolean updateStatus = cmProdInsInfoService.update(updateWrapper);
            if ( updateStatus ){
                cmProdInsInfoMapper.insert(cmProdInsInfo);
                return cmProdInsInfoMapper.selectByUserId(userId);
            }else {
                return null;
            }
        }else {
            cmProdInsInfoMapper.insert(cmProdInsInfo);
            return cmProdInsInfoMapper.selectByUserId(userId);
        }

    }

    @Override
    @CachePut(value = "cm_prod_ins_info", key = "#root.args[0]", unless="#result == null",cacheManager = "localRedisCacheManager")
    public List<CmProdInsInfo> updateDeleteProdInsInfo(Long userId,Date expireDate) {
        List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoMapper.selectByUserId(userId);
        log.info("CmProdInsInfoServiceImpl-updateDeleteProdInsInfo before cmProdInsInfoList is {}",cmProdInsInfoList);
        if ( cmProdInsInfoList.size() == 0 ){
            // 返回空，则说明数据库没有任何更新
            return null;
        }
        int status = cmProdInsInfoMapper.updateDeleteProdInsInfo(userId,expireDate);
        log.info("CmProdInsInfoServiceImpl-updateDeleteProdInsInfo update status is {}",status);
        if ( status > 0 ){
            return  cmProdInsInfoMapper.selectByUserId(userId);
        }else {
            return null;
        }
    }

    @Override
    public List<CmProdInsInfo> selectProdInsInfo(Date startTime, Date endTime) {
        return cmProdInsInfoMapper.selectProdInsInfo(startTime,endTime);
    }
}
