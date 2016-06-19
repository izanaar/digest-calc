package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.digest.DigestJobFactory;
import com.izanaar.digestCalc.digest.DigestRecursiveAction;
import com.izanaar.digestCalc.digest.JobStatusListener;
import com.izanaar.digestCalc.exception.JobServiceException;
import com.izanaar.digestCalc.repository.JobRepository;
import com.izanaar.digestCalc.repository.entity.Job;
import com.izanaar.digestCalc.repository.enums.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

@Service
public class JobService implements JobStatusListener {

    @Autowired
    private UUIDKeeper uuidKeeper;

    @Autowired
    private DigestJobFactory factory;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RecursiveActionExecutionService executionService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Job> getAll() {
        return jobRepository.findByUuid(uuidKeeper.getValue());
    }

    public Job getById(Long id) {
        return jobRepository.findOneByIdAndUuid(id, uuidKeeper.getValue());
    }

    public Job add(Job job) {
        job.setStatus(JobStatus.WAITING);
        job.setUuid(uuidKeeper.getValue());
        jobRepository.save(job);
        RecursiveAction action = factory.getRecursiveAction(job);
        executionService.executeAction(action, job.getId());

        return job;
    }

    public void cancel(Long id) {
        Optional<Job> jobOptional = Optional.ofNullable(jobRepository.findOneByIdAndUuid(id, uuidKeeper.getValue()));
        if(jobOptional.isPresent()){
            tryCancelJob(jobOptional.get());
        }else {
            throw new JobServiceException("Job not found. Invalid id.");
        }
    }

    private void tryCancelJob(Job job) {
        if (job.getStatus().equals(JobStatus.FAILED) || job.getStatus().equals(JobStatus.COMPLETED)){
            throw new JobServiceException("Couldn't cancel job that has already been done.");
        }else {
            if(job.getStatus().equals(JobStatus.PENDING) || !executionService.tryCancelActionExecutuion(job.getId())){
                throw new JobServiceException("Job processing has already been started.");
            }else{
                jobRepository.delete(job);
            }
        }
    }

    public void delete(Long id) {
        Optional<Job> jobOptional = Optional.ofNullable(jobRepository.findOneByIdAndUuid(id, uuidKeeper.getValue()));
        jobOptional.orElseThrow(() -> new JobServiceException("Invalid id."));

        Job job = jobOptional.get();
        if(job.getStatus().equals(JobStatus.WAITING) || job.getStatus().equals(JobStatus.PENDING)){
            throw new JobServiceException("Couldn't delete job with status " + job.getStatus() + ".");
        }else {
            jobRepository.delete(id);
        }
    }

    @Override
    public void notifyStart(Long id) {
        Optional<Job> jobOptional = Optional.ofNullable(jobRepository.findOne(id));
        if(jobOptional.isPresent()){
            Job job = jobOptional.get();
            job.setStatus(JobStatus.PENDING);
            job.setStartDate(new Date());
            jobRepository.save(job);
        }else{
            logger.error("Digest calculation has started for job that does not exist. ID: {}", id);
        }
    }

    @Override
    public void notifySuccess(Long id, String hex) {
        Optional<Job> jobOptional = Optional.ofNullable(jobRepository.findOne(id));
        if(jobOptional.isPresent()){
            Job job = jobOptional.get();
            job.setHex(hex);
            job.setStatus(JobStatus.COMPLETED);
            job.setEndDate(new Date());
            jobRepository.save(job);
        }else{
            logger.error("Digest calculation has ended for job that does not exist. ID: {}, hex: {}", id, hex);
        }
    }

    @Override
    public void notifyFailure(Long id, String stackTrace) {
        Optional<Job> jobOptional = Optional.ofNullable(jobRepository.findOne(id));
        if(jobOptional.isPresent()){
            Job job = jobOptional.get();
            job.setStatus(JobStatus.FAILED);
            job.setEndDate(new Date());
            jobRepository.save(job);
        }else{
            logger.error("Digest calculation has failed for job that does not exist. ID: {}, hex: {}", id, stackTrace);
        }
    }
}
