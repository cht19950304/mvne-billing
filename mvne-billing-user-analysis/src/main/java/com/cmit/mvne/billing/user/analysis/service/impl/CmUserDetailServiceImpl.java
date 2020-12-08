package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.user.analysis.entity.CmUserDetail;
import com.cmit.mvne.billing.user.analysis.mapper.CmUserDetailMapper;
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
public class CmUserDetailServiceImpl extends ServiceImpl<CmUserDetailMapper, CmUserDetail> implements CmUserDetailService {
    @Autowired
    private CmUserDetailMapper cmUserDetailMapper;

    @Override
    @CachePut(value = "cm_user_detail", key = "#root.args[0]", unless="#result == null",cacheManager = "localRedisCacheManager")
    public List<CmUserDetail> insert(String msisdn,CmUserDetail cmUserDetail) {
        List<CmUserDetail> cmUserDetailList = cmUserDetailMapper.findValidUser(msisdn);
        log.info("CmUserDetailServiceImpl-insert findValidUser cmUserDetailList is {}",cmUserDetailList);
        if ( cmUserDetailList.size() > 0 ){
            // 返回空，则说明数据库有生效的号码，有异常
            return null;
        }
        cmUserDetailMapper.insert(cmUserDetail);
        return cmUserDetailMapper.findAllByMsisdn(msisdn);
    }

	@Override
    @CachePut(value = "cm_user_detail", key = "#root.args[0]", unless="#result == null",cacheManager = "localRedisCacheManager")
	public List<CmUserDetail> updateChangeCard(String msisdn, CmUserDetail cmUserDetail) {
        List<CmUserDetail> cmUserDetailList = cmUserDetailMapper.findAllByMsisdn(msisdn);
        log.info("CmUserDetailServiceImpl-updateChangeCard before cmUserDetailList is {}",cmUserDetailList);
        if ( cmUserDetailList.size() == 0 ){
            // 返回空，则说明数据库没有任何更新
            return null;
        }
        Boolean userStatus = checkStartStopStatus(cmUserDetailList);
        log.info("CmUserDetailServiceImpl-updateChangeCard checkStartStopStatus userStatus is {}",userStatus);
        if ( userStatus ){
            int status = cmUserDetailMapper.updateChangeCard(cmUserDetail.getUserId(),msisdn,cmUserDetail.getValidDate(),"05");
            log.info("CmUserDetailServiceImpl-updateChangeCard updateChangeCard status is {}",status);
            if ( status > 0 ){
                cmUserDetailMapper.insert(cmUserDetail);
                log.info("CmUserDetailServiceImpl-updateChangeCard insert cmUserDetail is {}",cmUserDetail);
                return cmUserDetailMapper.findAllByMsisdn(msisdn);
            }else {
                return null;
            }
        }else {
            return null;
        }

	}

    @Override
    @CachePut(value = "cm_user_detail", key = "#root.args[0]", unless="#result == null",cacheManager = "localRedisCacheManager")
    public List<CmUserDetail> updateDeleteUserDetail(String msisdn, CmUserDetail cmUserDetail) {
        int status = cmUserDetailMapper.updateDeleteUserDetail(cmUserDetail.getUserId(),cmUserDetail.getValidDate());
        log.info("CmUserDetailServiceImpl-updateDeleteUserDetail update status is {}",status);
        if ( status > 0 ){
            return cmUserDetailMapper.findAllByMsisdn(msisdn);
        }else {
            return null;
        }
    }

    @Override
    public List<CmUserDetail> selectUserDetail(Date startTime, Date endTime) {
        return cmUserDetailMapper.selectUserDetail(startTime,endTime);
    }

    @Override
    @Cacheable(value = "cm_user_detail", key = "#root.args[0]", unless="#result == null",cacheManager = "selectRedisCacheManager")
    public List<CmUserDetail> findAllByMsisdn(String msisdn) {
        log.info("CmUserDetailServiceImpl-findAllByMsisdn check DB");
        return cmUserDetailMapper.findAllByMsisdn(msisdn);
    }

    @Override
    @CachePut(value = "cm_user_detail", key = "#root.args[0]", unless="#result == null",cacheManager = "localRedisCacheManager")
    public List<CmUserDetail> updateByMsisdn(String msisdn, CmUserDetail cmUserDetail) {
        List<CmUserDetail> cmUserDetailList = cmUserDetailMapper.findAllByMsisdn(msisdn);
        log.info("CmUserDetailServiceImpl-updateByMsisdn before cmUserDetailList is {}",cmUserDetailList);
        if ( cmUserDetailList.size() == 0 ){
            // 返回空，则说明数据库没有任何更新
            return null;
        }
        String beforeUserStatus = null;
        if ( cmUserDetail.getUserStatus().equals("02")){
            beforeUserStatus = "03";
        }else if ( cmUserDetail.getUserStatus().equals("03")){
            beforeUserStatus = "02";
        }
        Boolean userStatus = checkUserStatus(cmUserDetailList,beforeUserStatus);
        log.info("CmUserDetailServiceImpl-updateByMsisdn beforeUserStatus is {} , userStatus is {}",beforeUserStatus,userStatus);
        if ( userStatus ){
            int updateStatus = cmUserDetailMapper.updateExpire(cmUserDetail.getUserId(),msisdn,cmUserDetail.getValidDate(),beforeUserStatus);
            log.info("CmUserDetailServiceImpl-updateByMsisdn updateStatus is {}",updateStatus);
            if ( updateStatus > 0 ){
                cmUserDetailMapper.insert(cmUserDetail);
                log.info("CmUserDetailServiceImpl-updateByMsisdn insert cmUserDetail is {}",cmUserDetail);
                return cmUserDetailMapper.findAllByMsisdn(msisdn);
            }else {
                return null;
            }
        }else {
            return null;
        }

    }

    private Boolean checkUserStatus(List<CmUserDetail> cmUserDetailList,String beforeUserStatus){
        Boolean status = false;
        for(CmUserDetail cmUserDetail : cmUserDetailList){
            if ( cmUserDetail.getUserStatus().equals(beforeUserStatus) && cmUserDetail.getExpireDate().after(new Date())){
                status = true;
            }else {
                continue;
            }
        }
        return status;
    }

    private Boolean checkStartStopStatus(List<CmUserDetail> cmUserDetailList){
        Boolean status = false;
        for(CmUserDetail cmUserDetail : cmUserDetailList){
            if ( ( cmUserDetail.getUserStatus().equals("03") || cmUserDetail.getUserStatus().equals("02") ) && cmUserDetail.getExpireDate().after(new Date())){
                status = true;
            }else {
                continue;
            }
        }
        return status;
    }

}
