package com.example.desarrollo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    // Pool de hilos para los listeners @Async (correos y recálculo de estadísticas)
    @Override
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("muvu-async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);   // no perder correos al apagar la app
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    // Un @Async void no tiene a quién devolverle el error: queda registrado en el log
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error("Error en tarea asíncrona {}", method.getName(), ex);
    }
}
