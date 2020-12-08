package com.cmit.mvne.billing.infomanage.job.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.infomanage.job.entity.Job;

import java.util.List;

/**
 * @author MrBird
 */
public interface JobMapper extends BaseMapper<Job> {
	
	List<Job> queryList();
}