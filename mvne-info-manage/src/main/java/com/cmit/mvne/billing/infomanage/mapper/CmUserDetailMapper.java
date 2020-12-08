package com.cmit.mvne.billing.infomanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.infomanage.entity.CmUserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
@Mapper
@Repository
public interface CmUserDetailMapper extends BaseMapper<CmUserDetail> {
    int updateExpire(@Param("userId") Long userId, @Param("msisdn") String msisdn,@Param("expireDate") Date expireDate,@Param("userStatus") String userStatus);
}
