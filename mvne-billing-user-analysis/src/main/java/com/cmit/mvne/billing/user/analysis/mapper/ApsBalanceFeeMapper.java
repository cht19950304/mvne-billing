package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.ApsBalanceFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Mapper
@Repository
public interface ApsBalanceFeeMapper extends BaseMapper<ApsBalanceFee> {
    ApsBalanceFee selectByUserId(Long userId);
    //void updateBalanceFee(Long userId, Date expireDate);
    ApsBalanceFee selectBalanceFee();
    int updateBalance(Long userId, BigDecimal remainFee,Date updateTime);

}
