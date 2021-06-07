package com.example.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class KafkaInitialConfiguration {
    @Bean
    public NewTopic initialTopic(){
        return new NewTopic("testtopic", 8, (short) 1);
    }

    @Bean
    public NewTopic updateTopic(){
        return new NewTopic("testtopic", 10, (short) 1);
    }


}
