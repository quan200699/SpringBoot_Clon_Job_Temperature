package com.codegym.demo.repository;

import com.codegym.demo.model.CronJobTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobTaskRepository extends JpaRepository<CronJobTask, Long> {
}
