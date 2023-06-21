package net.mkuwan.batch.multijob;

import net.mkuwan.batch.multijob.job1.Job1Config;
import net.mkuwan.batch.multijob.job2.Job2Config;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MultiJobCommandLineJobRunner implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final Job job1;
    private final Job job2;

    /**
     *
     * @param jobLauncher
     * @param job1 job1,job2これがジョブ名と一致すると別々に取り扱えるみたい
     * @param job2
     */
    public MultiJobCommandLineJobRunner(JobLauncher jobLauncher, Job job1, Job job2) {
        this.jobLauncher = jobLauncher;
        this.job1 = job1;
        this.job2 = job2;
    }

    /**
     * コマンドラインの引数からの実行パターン
     * @param args incoming main method arguments
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
//        String arg = Job1Config.Job1Name;
        String arg = Job2Config.Job2Name;
        if(args.length > 0 && !args[0].isBlank())
            arg = args[0];

//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("multi-job", System.currentTimeMillis())
//                .toJobParameters();
//
//        if (arg.equals(Job1Config.Job1Name)) {
//            var execution = jobLauncher.run(job1, jobParameters);
//            System.out.println(execution.getStatus());
//        } else if (arg.equals(Job2Config.Job2Name)) {
//            var execution = jobLauncher.run(job2, jobParameters);
//            System.out.println(execution.getStatus());
//        } else {
//            var execution = jobLauncher.run(job1, jobParameters);
//            System.out.println(execution.getStatus());
//        }
    }
}
