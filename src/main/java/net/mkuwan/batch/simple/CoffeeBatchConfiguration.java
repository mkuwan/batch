package net.mkuwan.batch.simple;


import net.mkuwan.batch.model.Coffee;
import net.mkuwan.batch.tasklet.PersonTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * JobBuilderFactory and StepBuilderFactory are deprecated
 * it is recommended to use JobBuilder and StepBuilder classes
 * with the name of the job or step builder.
 */
@Configuration
//@EnableBatchProcessing
// これいれるとPreparedStatementCallback; bad SQL grammar
// [SELECT JOB_INSTANCE_ID, JOB_NAME from BATCH_JOB_INSTANCE where JOB_NAME = ? and JOB_KEY = ?]
// となってしまう
public class CoffeeBatchConfiguration {

    @Value(value = "${file.input}")
    private String fileInput;

    @Value(value = "${file.output}")
    private String fileOutput;

    @Bean
    @StepScope
    public FlatFileItemReader<Coffee> coffeeReader(){
        return new FlatFileItemReaderBuilder<Coffee>()
                .name("coffeeItemReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names("brand", "origin", "characteristics")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
                    setTargetType(Coffee.class);
                }})
                .build();
    }

    @Bean
    public CoffeeItemProcessor coffeeItemProcessor() {
        return new CoffeeItemProcessor();
    }

    @Bean
    public CoffeeItemProcessor2 coffeeItemProcessor2(){
        return new CoffeeItemProcessor2();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Coffee> coffeeWriter(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Coffee>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO coffee (brand, origin, characteristics) VALUES (:brand, :origin, :characteristics)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Coffee> coffeeJdbcCursorItemReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<Coffee>()
                .sql("Select * fom coffee")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    @Qualifier("importUserJob")
    public Job importUserJob(JobRepository jobRepository,
                             CoffeeJobCompletionNotificationListener listener,
                             Step step1, Step step2){
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
//                .preventRestart()   // restartable=false
                .listener(listener)
                .flow(step1)
                .next(step2)
                .end()
                .build();
    }


    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcBatchItemWriter<Coffee> writer){
        return new StepBuilder("step1", jobRepository)
                .<Coffee, Coffee> chunk(10, transactionManager)
                .reader(coffeeReader())
                .processor(coffeeItemProcessor())
                .writer(writer)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcBatchItemWriter<Coffee> writer){
        return new StepBuilder("step2", jobRepository)
                .<Coffee, Coffee> chunk(10, transactionManager)
                .reader(coffeeReader())
                .processor(coffeeItemProcessor2())
                .writer(writer)
                .build();
    }

    //-------------------- tasklet ------------------------

    @Bean
    @Qualifier("jobTasklet")
    public Job jobTasklet(JobRepository jobRepository,
                   CoffeeJobCompletionNotificationListener listener,
                   Step step4) {
        return new JobBuilder("jobTasklet", jobRepository)
                .listener(listener)
                .start(step4)
                .build();
    }

    @Bean
    @StepScope
    public PersonTasklet personTasklet(){
        return new PersonTasklet();
    }

    @Bean
    public Step step4(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager){
        return new StepBuilder("tasklet_step", jobRepository)
                .tasklet(personTasklet(), transactionManager)
                .build();

    }
}
