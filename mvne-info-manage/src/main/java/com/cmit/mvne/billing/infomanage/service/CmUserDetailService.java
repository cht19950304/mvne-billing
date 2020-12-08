package com.cmit.mvne.billing.infomanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.infomanage.entity.CmUserDetail;

import java.util.Date;

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
    void insert(CmUserDetail cmUserDetail);
    
    /**
     * @Description:根据userid更新cm_user_detail表的imsi信息
     * @param cmUserDetail
     * @param userid
     */
    void updateChangeCard(CmUserDetail cmUserDetail,Long userid);
    int updateExpire(Long userId,String msisdn, Date expireDate,String userStatus);

}
