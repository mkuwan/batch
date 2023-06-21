package net.mkuwan.batch.simple;

import net.mkuwan.batch.model.Coffee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CoffeeItemProcessor2 implements ItemProcessor<Coffee, Coffee> {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeItemProcessor.class);

    @Override
    public Coffee process(final Coffee coffee) throws Exception {
        String brand = "ブランド:" + coffee.getBrand().toLowerCase();
        String origin = "原産地:" + coffee.getOrigin().toLowerCase();
        String characteristics = "特徴:" + coffee.getCharacteristics().toLowerCase();

        Coffee transformedCoffee = new Coffee(brand, origin, characteristics);
        logger.info("{}を{}に変換した", coffee, transformedCoffee);
        return transformedCoffee;
    }
}
