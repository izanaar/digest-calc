package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.digest.DigestJobFactory;
import com.izanaar.digestCalc.digest.JobStatusListener;
import com.izanaar.digestCalc.exception.JobServiceException;
import com.izanaar.digestCalc.repository.JobRepository;
import com.izanaar.digestCalc.repository.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService implements JobStatusListener {

    @Autowired
    private UUIDKeeper uuidKeeper;

    @Autowired
    private DigestJobFactory factory;

    @Autowired
    private JobRepository jobRepository;

    public List<Job> getAll(){
        return jobRepository.findAll();
    }

    public Job getById(Long id) {
        return null;
    }

    public Job add(Job job){
        return null;
    }

    public void cancel(Long id) throws JobServiceException {

    }

    public void delete(Long id)  throws JobServiceException {

    }

    @Override
    public void notifyStart(Long id) {

    }

    @Override
    public void notifySuccess(Long id, String hex) {

    }

    @Override
    public void notifyFailure(Long id, String stackTrace) {

    }
}
