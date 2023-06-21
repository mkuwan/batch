package net.mkuwan.batch.multijob.job1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class Tasklet02 implements Tasklet {
    private static final Logger logger = LoggerFactory.getLogger(Tasklet02.class);
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Tasklet02の処理を行います {}", "2秒間");
        Thread.sleep(1000 * 2);
        logger.info("Tasklet02の処理が完了しました");

        return RepeatStatus.FINISHED;
    }
}
