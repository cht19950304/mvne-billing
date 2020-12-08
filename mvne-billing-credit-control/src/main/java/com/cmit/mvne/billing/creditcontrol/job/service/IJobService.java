package com.cmit.mvne.billing.creditcontrol.job.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.creditcontrol.job.entity.Job;

import org.springframework.data.domain.Pageable;

/**
 * @author MrBird
 */
public interface IJobService extends IService<Job> {

    Job findJob(Long jobId);

    IPage<Job> findJobs(Pageable pageable, Job job);

    void createJob(Job job);

    void updateJob(Job job);

    void deleteJobs(String[] jobIds);

    int updateBatch(String jobIds, String status);

    void run(String jobIds);

    void pause(String jobIds);

    void resume(String jobIds);
}
