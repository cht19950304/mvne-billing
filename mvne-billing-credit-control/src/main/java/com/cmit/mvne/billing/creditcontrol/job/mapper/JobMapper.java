package com.cmit.mvne.billing.creditcontrol.job.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.creditcontrol.job.entity.Job;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author MrBird
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {
	
	List<Job> queryList();
}