package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementJacksonCopy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName can't be null");
        }
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        var loadedMeasurements = new ArrayList<Measurement>();
        var mapper = new ObjectMapper();
        try (var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ResourcesFileLoader.class.
                getClassLoader().getResourceAsStream(fileName))))) {
            var builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            line = builder.substring(2, builder.length() - 2);
            String[] entries = line.split("},\\{");
            for (var entry : entries) {
                var object = mapper.readValue("{" + entry + "}", MeasurementJacksonCopy.class);
                loadedMeasurements.add(new Measurement(object.getName(), object.getValue()));
            }
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
        return loadedMeasurements;
    }
}
