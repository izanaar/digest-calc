package com.izanaar.digestCalc.repository;

import com.izanaar.digestCalc.repository.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByUuid(String uuid);

    Job findOneByIdAndUuid(Long id, String uuid);
}
