package com.cmit.mvne.billing.user.analysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.user.analysis.entity.CmProdInsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface CmProdInsInfoMapper extends BaseMapper<CmProdInsInfo> {
    int updateDeleteProdInsInfo(@Param("userId") Long userId, @Param("expireDate") Date expireDate );
    List<CmProdInsInfo> selectProdInsInfo(Date startTime, Date endTime);
    List<CmProdInsInfo> selectByUserId(@Param("userId") Long userId);
    List<CmProdInsInfo> findValidProd(@Param("userId") Long userId);

}
