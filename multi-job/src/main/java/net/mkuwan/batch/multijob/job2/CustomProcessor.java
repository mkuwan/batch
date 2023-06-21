package net.mkuwan.batch.multijob.job2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

public class CustomProcessor implements ItemProcessor<String, String> {

    private final static Logger logger = LoggerFactory.getLogger(CustomItemReader.class);

    @Override
    public String process(String item) throws Exception {
        logger.info("Custom Processor です {}", item);
        Thread.sleep(1000 * 1);
        return item.toUpperCase();
    }
}
