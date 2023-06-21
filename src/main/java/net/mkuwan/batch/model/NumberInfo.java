package net.mkuwan.batch.model;

import lombok.Data;

@Data
public class NumberInfo {

    private Integer number;

    public boolean isPositive() {
        return number > 0;
    }
}
