package com.cmit.mvne.billing.creditcontrol.job.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.creditcontrol.job.entity.JobLog;

import org.springframework.data.domain.Pageable;

/**
 * @author MrBird
 */
public interface IJobLogService extends IService<JobLog> {

    IPage<JobLog> findJobLogs(Pageable pageable, JobLog jobLog);

    void saveJobLog(JobLog log);

    void deleteJobLogs(String[] jobLogIds);
}
