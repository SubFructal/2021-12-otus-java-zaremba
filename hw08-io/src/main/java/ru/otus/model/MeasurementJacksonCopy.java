package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeasurementJacksonCopy {
    private final String name;
    private final double value;

    @JsonCreator
    public MeasurementJacksonCopy(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
