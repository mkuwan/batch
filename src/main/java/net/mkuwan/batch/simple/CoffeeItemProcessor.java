package net.mkuwan.batch.simple;

import net.mkuwan.batch.model.Coffee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CoffeeItemProcessor implements ItemProcessor<Coffee, Coffee> {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeItemProcessor.class);

    @Override
    public Coffee process(final Coffee coffee) throws Exception {
        String brand = coffee.getBrand().toUpperCase();
        String origin = coffee.getOrigin().toUpperCase();
        String characteristics = coffee.getCharacteristics().toUpperCase();

        Coffee transformedCoffee = new Coffee(brand, origin, characteristics);
        logger.info("{}を{}に変換したよ", coffee, transformedCoffee);
        return transformedCoffee;
    }
}
