package com.cmit.mvne.billing.creditcontrol.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.creditcontrol.entity.CmUserDetail;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jiangxm
 * @since 2020-02-17
 */
@Mapper
@Repository
public interface CmUserDetailMapper extends BaseMapper<CmUserDetail> {
	
}
