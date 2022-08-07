package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorThrowsEvenSecondExceptionTest {

    @Test
    void shouldThrowEvenSecondException() {
        var processor = new ProcessorThrowsEvenSecondException(() -> LocalDateTime.of(2022, 7, 20, 1, 0, 34));
        assertThrows(RuntimeException.class, () -> processor.process(null));
    }

    @Test
    void shouldNotThrowEvenSecondException() {
        var processor = new ProcessorThrowsEvenSecondException(() -> LocalDateTime.of(2022, 7, 20, 1, 0, 37));
        assertDoesNotThrow(() -> processor.process(null));
    }

}