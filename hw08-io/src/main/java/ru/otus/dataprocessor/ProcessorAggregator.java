package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        Map<String, List<Measurement>> sortedMeasurements = data.stream().collect(Collectors.groupingBy(Measurement::getName));
        Map<String, Double> aggregatedMeasurements = new TreeMap<>();
        for (var pair : sortedMeasurements.entrySet()) {
            double sum = 0;
            for (var entry : pair.getValue()) {
                sum += entry.getValue();
            }
            aggregatedMeasurements.put(pair.getKey(), sum);
        }
        return aggregatedMeasurements;
    }
}
