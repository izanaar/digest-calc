package com.izanaar.digestCalc.repository;

import com.izanaar.digestCalc.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
