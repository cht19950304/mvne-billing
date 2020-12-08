package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.ApsFreeRes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
public interface ApsFreeResService extends IService<ApsFreeRes> {
        List<ApsFreeRes> findByUserId(Long userId);

        List<ApsFreeRes> findByUserIdProdIns(Long userId,Long productInsId);

        void insert(ApsFreeRes apsFreeRes);

        int updateDeleteFreeRes(Long userId, Date expireDate);

        int updateFreeRes(Long userId, Long productInstId, Long itemId, BigDecimal usedValue);

        List<ApsFreeRes> selectFreeRes(Date startTime, Date endTime);

        ApsFreeRes selectByKey(Long userId, Long productInsId, Long itemId);

        List<ApsFreeRes> findValidFreeRes(Long userId);
}
