package net.mkuwan.batch;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;

import java.util.Collection;

@SpringBootTest
@SpringBatchTest
class BatchApplicationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;


    @AfterEach
    void cleanUp(){
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private JobParameters defaultJobParameters(){
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
//        parametersBuilder.addString("file.input", TEST_INPUT);
        return parametersBuilder.toJobParameters();
    }



    @Test
    public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
//        // given
//        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
//        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
        Assertions.assertEquals("importUserJob", actualJobInstance.getJobName());
        Assertions.assertEquals("COMPLETED", actualJobExitStatus.getExitCode());

    }

    /**
     * Testing Individual Steps
     * @throws Exception
     */
    @Test
    public void givenReferenceOutput_whenStep1Executed_thenSuccess() throws Exception {
//        // given
//        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
//        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1");
        Collection actualStepExecutions = jobExecution.getStepExecutions();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
        Assertions.assertEquals(1, actualStepExecutions.size());
        Assertions.assertEquals("COMPLETED", actualJobExitStatus.getExitCode());
    }

    @Test
    public void givenReferenceOutput_whenStep2Executed_thenSuccess() throws Exception {
//        // given
//        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
//        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("step2");
        Collection actualStepExecutions = jobExecution.getStepExecutions();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
        Assertions.assertEquals(1, actualStepExecutions.size());
        Assertions.assertEquals("COMPLETED", actualJobExitStatus.getExitCode());
    }
}
