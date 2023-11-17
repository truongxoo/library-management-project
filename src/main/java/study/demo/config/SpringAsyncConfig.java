package study.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class SpringAsyncConfig {

//    @Bean(name = "threadPoolTaskExecutor")
//    Executor threadPoolTaskExecutor() {
//        return new ThreadPoolTaskExecutor();
//    }
    
    @Bean
    TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
    
   
} 