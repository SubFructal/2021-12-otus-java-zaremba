package ru.otus.processor.homework;

import lombok.extern.slf4j.Slf4j;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

@Slf4j
public class ProcessorChangeFieldValues implements Processor {

    @Override
    public Message process(Message message) {
        log.info("input message: {}", message);
        message = message.toBuilder().field11(message.getField12()).field12(message.getField11()).build();
        log.info("output message: {}", message);
        return message;
    }
}
