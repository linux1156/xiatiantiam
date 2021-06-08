package com.example.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class KafkaProducer{
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(Object obj){
        kafkaTemplate.send("topic_xiaoxia_test",obj.toString());
    }

}
