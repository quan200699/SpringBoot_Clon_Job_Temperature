package com.codegym.demo.service.cron;

import com.codegym.demo.model.CronJobTask;
import com.codegym.demo.repository.CronJobTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CronJobTaskService implements ICronJobTaskService {
    @Autowired
    private CronJobTaskRepository cronJobTaskRepository;

    @Override
    public Iterable<CronJobTask> findAll() {
        return cronJobTaskRepository.findAll();
    }

    @Override
    public Optional<CronJobTask> findById(Long id) {
        return cronJobTaskRepository.findById(id);
    }

    @Override
    public CronJobTask save(CronJobTask cronJobTask) {
        return cronJobTaskRepository.save(cronJobTask);
    }

    @Override
    public void remove(Long id) {
        cronJobTaskRepository.deleteById(id);
    }
}
