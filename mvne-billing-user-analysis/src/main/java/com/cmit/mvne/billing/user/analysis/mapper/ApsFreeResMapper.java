package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.ApsFreeRes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
public interface ApsFreeResMapper extends BaseMapper<ApsFreeRes> {
    int updateDeleteFreeRes(Long userId, Date expireDate);
    List<ApsFreeRes> findByUserId(Long userId);
    List<ApsFreeRes> findByUserIdProdIns(Long userId,Long productInsId);
    List<ApsFreeRes> selectFreeRes(Date startTime, Date endTime);
    List<ApsFreeRes> findValidFreeRes(Long userId);
}
