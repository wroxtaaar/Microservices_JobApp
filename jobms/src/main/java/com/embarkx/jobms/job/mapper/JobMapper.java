package com.embarkx.jobms.job.mapper;

import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.dto.JobDTO;
import com.embarkx.jobms.job.external.Company;
import com.embarkx.jobms.job.external.Review;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDTO (Job job, Company company,
                                                 List<Review> reviews) {
        return new JobDTO(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getMinSalary(),
                job.getMaxSalary(),
                job.getLocation(),
                job.getCompanyId(),
                company,
                reviews
        );
    }
}
