package net.mkuwan.batch.multijob.kafka;

import net.mkuwan.batch.multijob.job1.Job1Config;
import net.mkuwan.batch.multijob.job2.Job2Config;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListen {
    private static final Logger logger = LoggerFactory.getLogger(KafkaListen.class);

    private final JobLauncher jobLauncher;
    private final Job job1;
    private final Job job2;

    public KafkaListen(JobLauncher jobLauncher, Job job1, Job job2) {
        this.jobLauncher = jobLauncher;
        this.job1 = job1;
        this.job2 = job2;
    }

    /**
     *
     * @param data
     * @throws Exception
     *  JobInstanceAlreadyCompleteException,
     *  JobExecutionAlreadyRunningException,
     *  JobParametersInvalidException,
     *  JobRestartException
     */
    @KafkaListener(topics = "multi-job-topic-produce")
    public void listen(ConsumerRecord<String, String> data) throws Exception {
        logger.info("Kafkaリスナーで受信しました {}", data);
        if(data.value().equals(Job1Config.Job1Name)){
            logger.info("Kafkaリスナーからジョブ1を実行します");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("multi-job", System.currentTimeMillis())
                    .toJobParameters();
            var execution = jobLauncher.run(job1, jobParameters);
            logger.info(String.valueOf(execution.getStatus()));
        }

        if(data.value().equals(Job2Config.Job2Name)){
            logger.info("Kafkaリスナーからジョブ2を実行します");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("multi-job", System.currentTimeMillis())
                    .toJobParameters();
            var execution = jobLauncher.run(job2, new JobParameters());
            logger.info(String.valueOf(execution.getStatus()));
        }
    }

}
