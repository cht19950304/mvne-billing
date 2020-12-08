package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.ApsBalanceFee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
public interface ApsBalanceFeeService extends IService<ApsBalanceFee> {
        void insert(ApsBalanceFee apsBalanceFee);
        ApsBalanceFee selectBalanceFee();
        int updateBalance(Long userId, BigDecimal remainFee, Date updateTime);
        ApsBalanceFee selectByUserId(Long userId);
}
