package net.mkuwan.batch.multijob.job2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;


public class CustomItemReader implements ItemReader<String> {

    private final static Logger logger = LoggerFactory.getLogger(CustomItemReader.class);

    private static Integer count = 1;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        logger.info("Custom Item Reader です {}回目", count);
        Thread.sleep(1000 * 1);

        if(count > 2)
            return null;

        count++;
        return "Item Reader";
    }
}
