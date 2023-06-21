package net.mkuwan.batch.simple;

import net.mkuwan.batch.model.Coffee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CoffeeJobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(CoffeeJobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CoffeeJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            logger.info("コーヒージョブが完了したよ");

            String query = "SELECT brand, origin, characteristics FROM coffee";
            jdbcTemplate.query(query, (rs, rowNum) ->
                    new Coffee(rs.getString(1), rs.getString(2), rs.getString(3)))
                    .forEach(coffee -> logger.info("DBに{}を保存してたよ", coffee));
        }
    }
}
