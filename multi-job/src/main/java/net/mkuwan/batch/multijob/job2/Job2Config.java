package net.mkuwan.batch.multijob.job2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class Job2Config {
    public static final String Job2Name = "Job2";
    private static final Logger logger = LoggerFactory.getLogger(Job2Config.class);

    @Bean
    public Job job2(JobRepository jobRepository,
                    Job2Listener listener,
                    Step step001){
        return new JobBuilder(Job2Name, jobRepository)
                .incrementer(new RunIdIncrementer())
//                .preventRestart()   // 再起動の禁止
                .listener(listener)
                .start(step001)
//                .end()
                .build();
    }

    @Bean
    public Step step001(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager){
        return new StepBuilder("job2-step1", jobRepository)
                .<String, String>chunk(1,transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public CustomItemReader reader(){
        return new CustomItemReader();
    }

    @Bean
    public CustomProcessor processor(){
        return new CustomProcessor();
    }

    @Bean
    public CustomItemWriter writer(){
        return new CustomItemWriter();
    }

}
