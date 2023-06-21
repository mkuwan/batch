package net.mkuwan.batch.multijob.job2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


public class CustomItemWriter implements ItemWriter<String> {

    private final static Logger logger = LoggerFactory.getLogger(CustomItemReader.class);

    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        logger.info("Custom Item Writer です {}", chunk.getItems().get(0));
        Thread.sleep(1000 * 1);
    }
}
