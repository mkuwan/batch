package net.mkuwan.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JobLauncherService {
    private final JobLauncher jobLauncher;

    @Autowired
    public JobLauncherService(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public void runJob(String qualifier){
        if(qualifier == "importUserJob"){
//            final JobExecution jobExecution = jobLauncher.run()

        }

        if(qualifier == "jobTasklet"){

        }
    }
}
