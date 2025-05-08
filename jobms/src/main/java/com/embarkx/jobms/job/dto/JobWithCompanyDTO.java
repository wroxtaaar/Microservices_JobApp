package com.embarkx.jobms.job.dto;

import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.external.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class JobWithCompanyDTO {
    private Job job;
    private Company company;


}
