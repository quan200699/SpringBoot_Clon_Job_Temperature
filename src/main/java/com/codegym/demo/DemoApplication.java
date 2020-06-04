package com.codegym.demo;

import com.codegym.demo.model.CronJobTask;
import com.codegym.demo.service.cron.ICronJobTaskService;
import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {
    @Autowired
    private ICronJobTaskService cronJobTaskService;

    @Bean
    public Messenger messenger(@Value("${messenger4j.pageAccessToken}") String pageAccessToken,
                               @Value("${messenger4j.appSecret}") final String appSecret,
                               @Value("${messenger4j.verifyToken}") final String verifyToken) {
        return Messenger.create(pageAccessToken, appSecret, verifyToken);
    }


    @PostConstruct
    public void init() {
        List<CronJobTask> cronJobTasks = (List<CronJobTask>) cronJobTaskService.findAll();
        if (cronJobTasks.isEmpty()) {
            Long oneMinute = 1000 * 60L;
            Long oneHour = 1000 * 60 * 60L;
            Long oneDay = 1000 * 60 * 60 * 24L;
            CronJobTask cronJobTask1 = new CronJobTask();
            cronJobTask1.setTime(oneMinute * 10);
            cronJobTask1.setStatus(false);
            cronJobTaskService.save(cronJobTask1);
            CronJobTask cronJobTask2 = new CronJobTask();
            cronJobTask2.setTime(oneHour);
            cronJobTask2.setStatus(false);
            cronJobTaskService.save(cronJobTask2);
            CronJobTask cronJobTask3 = new CronJobTask();
            cronJobTask3.setTime(oneHour * 3);
            cronJobTask3.setStatus(false);
            cronJobTaskService.save(cronJobTask3);
            CronJobTask cronJobTask4 = new CronJobTask();
            cronJobTask4.setTime(oneHour * 6);
            cronJobTask4.setStatus(false);
            cronJobTaskService.save(cronJobTask4);
            CronJobTask cronJobTask5 = new CronJobTask();
            cronJobTask5.setTime(oneDay * 3);
            cronJobTask5.setStatus(false);
            cronJobTaskService.save(cronJobTask5);
            CronJobTask cronJobTask6 = new CronJobTask();
            cronJobTask6.setTime(oneDay * 7);
            cronJobTask6.setStatus(false);
            cronJobTaskService.save(cronJobTask6);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
