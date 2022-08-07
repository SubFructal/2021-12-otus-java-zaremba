package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName can't be null");
        }
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        try (var writer = new BufferedWriter(new FileWriter(fileName))) {
            var mapper = new ObjectMapper();
            var jsonData = mapper.writeValueAsString(data);
            writer.write(jsonData);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
