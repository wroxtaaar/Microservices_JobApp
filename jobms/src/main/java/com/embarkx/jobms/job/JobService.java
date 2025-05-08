package com.embarkx.jobms.job;

import com.embarkx.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {

    List<JobDTO> findAll();
    JobDTO getJobById(Long id);
    void createJob(Job job);
    boolean deleteJobById(Long id);
    boolean updateJobById(Long id, Job job);

}
