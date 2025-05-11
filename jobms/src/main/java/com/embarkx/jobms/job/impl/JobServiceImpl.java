package com.embarkx.jobms.job.impl;

import com.embarkx.jobms.job.Job;
import com.embarkx.jobms.job.JobRepository;
import com.embarkx.jobms.job.JobService;
import com.embarkx.jobms.job.clients.CompanyClient;
import com.embarkx.jobms.job.clients.ReviewClient;
import com.embarkx.jobms.job.dto.JobDTO;
import com.embarkx.jobms.job.external.Company;
import com.embarkx.jobms.job.external.Review;
import com.embarkx.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
//@RequiredArgsConstructor
//@AllArgsConstructor
@Service
public class JobServiceImpl implements JobService {

    JobRepository jobRepository;
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;
    int attempt = 0;

    public JobServiceImpl(ReviewClient reviewClient, CompanyClient companyClient, JobRepository jobRepository) {
        this.reviewClient = reviewClient;
        this.companyClient = companyClient;
        this.jobRepository = jobRepository;
    }

//    @Override
//    @CircuitBreaker(name="companyBreaker",
//            fallbackMethod = "companyBreakerFallback")
//    @Override
//    @Retry(name="companyBreaker",
//            fallbackMethod = "companyBreakerFallback")
    @Override
    @RateLimiter(name="companyBreaker",
            fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        log.info("Attempt: {}", ++attempt);
        return jobs.stream().map(this::convertToDto).toList();
    }

    public List<String> companyBreakerFallback(Exception e) {
        List<String> fallbackList = new ArrayList<>();
        fallbackList.add("Dummy");
        return fallbackList;
    }

    private JobDTO convertToDto(Job job) {
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
        return JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
    }


    //        Company company = restTemplate.getForObject(
//                "http://COMPANY-SERVICE:8081/companies/" + job.getCompanyId(), Company.class);

//        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
//                "http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Review>>() {
//                });

//        List<Review> reviews = reviewResponse.getBody();


    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return job == null ? null : convertToDto(job);
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
        if(jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }        // If the company with the given ID does not exist, return false
        return false;
    }

    @Override
        public boolean updateJobById(Long id, Job updatedJob) {

        Optional<Job> jobOptional = jobRepository.findById(id);

        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setLocation(updatedJob.getLocation());
            job.setCompanyId(updatedJob.getCompanyId());
            jobRepository.save(job);
            return true;
        }

        return false;
    }
}
