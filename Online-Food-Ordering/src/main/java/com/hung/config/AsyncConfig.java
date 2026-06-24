package com.hung.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("syncExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        ex.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        ex.setQueueCapacity(500);
        ex.setThreadNamePrefix("es-sync-");
        ex.initialize();
        return ex;
    }
}