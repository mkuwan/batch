package net.mkuwan.batch.multijob.job1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class Job1Listener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(Job1Listener.class);

    @Override
    public void beforeJob(JobExecution jobExecution){
        logger.info("Job1Listener: beforeJobです {}", jobExecution.getStatus());
    }

    @Override
    public void afterJob(JobExecution jobExecution){
//        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
//            logger.info("Job1Listener: afterJobです");
//        }
        logger.info("Job1Listener: afterJobです {}", jobExecution.getStatus());
    }
}
