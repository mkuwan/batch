package net.mkuwan.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class PersonTasklet implements Tasklet {

    private final static Logger logger = LoggerFactory.getLogger(PersonTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try {
            logger.info("10秒間スリープします");
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1 * 1000);
                logger.info(i + 1 + "秒経過");
            }
            logger.info("スリープ終了しました");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return RepeatStatus.FINISHED;
    }
}
