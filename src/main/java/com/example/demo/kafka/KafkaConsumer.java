package com.example.demo.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = {"topic_xiaoxia_test"})
    public void topic_xiaoxia_test(ConsumerRecord<?, ?>record){
        log.info(String.valueOf(record.partition()));
        log.info(record.value().toString());
        log.info(record.topic());
    }


}
