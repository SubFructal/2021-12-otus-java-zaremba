package ru.otus.processor.homework;

import lombok.extern.slf4j.Slf4j;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.temporal.ChronoField;

@Slf4j
public class ProcessorThrowsEvenSecondException implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowsEvenSecondException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        var secondNumber = dateTimeProvider.getDate().get(ChronoField.SECOND_OF_MINUTE);
        if (secondNumber % 2 == 0) {
            log.info("even second:{}", secondNumber);
            throw new RuntimeException();
        }
        log.info("odd second:{}", secondNumber);
        return message;
    }
}
