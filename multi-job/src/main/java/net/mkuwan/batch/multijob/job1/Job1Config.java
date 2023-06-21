package net.mkuwan.batch.multijob.job1;

import net.mkuwan.batch.multijob.job2.CustomItemReader;
import net.mkuwan.batch.multijob.job2.CustomItemWriter;
import net.mkuwan.batch.multijob.job2.CustomProcessor;
import net.mkuwan.batch.multijob.job2.Job2Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class Job1Config {
    public static final String Job1Name = "Job1";
    private static final Logger logger = LoggerFactory.getLogger(Job1Config.class);

    @Bean
    public Job job1(JobRepository jobRepository,
                    Job1Listener listener,
                    Step step1, Step step2){
        return new JobBuilder(Job1Name, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("job1-step1", jobRepository)
                .tasklet(tasklet01(), transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Tasklet01 tasklet01(){
        return new Tasklet01();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("job1-step2", jobRepository)
                .tasklet(tasklet02(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet02 tasklet02(){
        return new Tasklet02();
    }
}
