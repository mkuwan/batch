package net.mkuwan.batch.multijob;

import net.mkuwan.batch.multijob.job1.Job1Config;
import net.mkuwan.batch.multijob.job2.Job2Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBatchTest
class MultiJobApplicationTests {
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private Job job1;

	@Autowired
	private Job job2;

	@Test
	public void Job1Test() throws Exception {
		// Arrange
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("multi-job", System.currentTimeMillis())
				.toJobParameters();
		jobLauncherTestUtils.setJob(job1);

		// Act
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		// Assert
		Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	@Test
	public void Job2Test() throws Exception {
		// Arrange
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("multi-job", System.currentTimeMillis())
				.toJobParameters();
		jobLauncherTestUtils.setJob(job2);

		// Act
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		// Assert
		Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}
}
