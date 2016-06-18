package com.izanaar.digestCalc.repository;

import com.izanaar.digestCalc.repository.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

}
