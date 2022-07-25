package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    private static final int THREAD_POOL_SIZE = 4;

    @Bean
    public ExecutorService executor() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }
}
