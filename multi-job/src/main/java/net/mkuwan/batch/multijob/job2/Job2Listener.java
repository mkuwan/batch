package net.mkuwan.batch.multijob.job2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class Job2Listener implements JobExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(Job2Listener.class);

    @Override
    public void beforeJob(JobExecution jobExecution){
        logger.info("Job2Listener: beforeJobです {}", jobExecution.getStatus());
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        logger.info("Job2Listener: afterJobです {}", jobExecution.getStatus());
    }
}
