package net.mkuwan.batch.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class Coffee {
    private String brand;
    private String origin;
    private String characteristics;

    public Coffee(){}

    public Coffee(String brand, String origin, String characteristics) {
        this.brand = brand;
        this.origin = origin;
        this.characteristics = characteristics;
    }


}
