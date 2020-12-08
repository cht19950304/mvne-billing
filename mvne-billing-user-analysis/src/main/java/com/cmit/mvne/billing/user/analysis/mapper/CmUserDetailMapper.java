package com.cmit.mvne.billing.user.analysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.user.analysis.entity.CmUserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
    int updateChangeCard(@Param("userId") Long userId, @Param("msisdn") String msisdn,@Param("expireDate") Date expireDate,@Param("userStatus") String userStatus);
    int updateDeleteUserDetail(@Param("userId") Long userId,@Param("expireDate") Date expireDate );
    List<CmUserDetail> selectUserDetail(Date startTime, Date endTime);
    List<CmUserDetail> findAllByMsisdn(@Param("msisdn") String msisdn);
    List<CmUserDetail> findValidUser(@Param("msisdn") String msisdn);
}
