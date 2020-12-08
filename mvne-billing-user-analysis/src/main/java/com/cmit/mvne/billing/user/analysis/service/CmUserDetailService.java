package com.cmit.mvne.billing.user.analysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.user.analysis.entity.CmUserDetail;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
public interface CmUserDetailService extends IService<CmUserDetail> {
    /**
     * 将对象查询cm_user_detail表
     * @param cmUserDetail
     */
    List<CmUserDetail> insert(String msisdn,CmUserDetail cmUserDetail);
    //void updateChangeCard(CmUserDetail cmUserDetail,Long userid);
    List<CmUserDetail> updateChangeCard(String msisdn, CmUserDetail cmUserDetail);
    List<CmUserDetail> updateDeleteUserDetail(String msisdn, CmUserDetail cmUserDetail);
    List<CmUserDetail> selectUserDetail(Date startTime, Date endTime);
    List<CmUserDetail> findAllByMsisdn(String msisdn);
    List<CmUserDetail> updateByMsisdn(String msisdn,CmUserDetail cmUserDetail);

}
