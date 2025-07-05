package com.intimetec.newsaggregation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfiguration {

    private final String NAMING_PATTERN_PREFIX = "AsyncExecutor-";

    @Bean
    public ThreadPoolTaskExecutor asyncExecutor() {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(availableProcessors);
        executor.setMaxPoolSize(availableProcessors);
        executor.setQueueCapacity(10);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix(NAMING_PATTERN_PREFIX);
        executor.initialize();
        return executor;
    }

}
