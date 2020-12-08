package com.cmit.mvne.billing.user.analysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.user.analysis.entity.CmProdInsInfo;

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
public interface CmProdInsInfoService extends IService<CmProdInsInfo> {
    List<CmProdInsInfo> findAllByUserId(Long userId);
    List<CmProdInsInfo> insertCreate(Long userId,CmProdInsInfo cmProdInsInfo);
    List<CmProdInsInfo> insertOffer(Long userId,CmProdInsInfo cmProdInsInfo);
    List<CmProdInsInfo> updateDeleteProdInsInfo(Long userId,Date expireDate);
    List<CmProdInsInfo> selectProdInsInfo(Date startTime, Date endTime);
}
