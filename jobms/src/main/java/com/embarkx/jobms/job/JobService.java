package com.embarkx.jobms.job;

import com.embarkx.jobms.job.dto.JobWithCompanyDTO;

import java.util.List;

public interface JobService {

    List<JobWithCompanyDTO> findAll();
    Job getJobById(Long id);
    void createJob(Job job);
    boolean deleteJobById(Long id);
    boolean updateJobById(Long id, Job job);

}
