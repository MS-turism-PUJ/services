package com.turism.services.services;

import com.google.gson.Gson;
import com.turism.services.dtos.ContentMessageDTO;
import com.turism.services.dtos.ServiceMessageDTO;

import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MessageQueueService {
    private static final String servicesQueueName = "servicesQueue";
    private static final String contentsQueueName = "contentsQueue";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MessageQueueService() {
        log.info("Creating Kafka producer");
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);

        configProps.put(ProducerConfig.ACKS_CONFIG, "1");

        ProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    public void sendServiceMessage(ServiceMessageDTO service) {
        log.info("Sending message to Kafka");
        Gson gson = new Gson();
        try {
            kafkaTemplate.send(servicesQueueName, gson.toJson(service));
            log.info("Message sent: {}", servicesQueueName);
        } catch (Exception e) {
            log.error("Error sending message", e);
        }
    }

    public void sendContentMessage(ContentMessageDTO content) {
        log.info("Sending message to Kafka");
        Gson gson = new Gson();
        try {
            kafkaTemplate.send(contentsQueueName, gson.toJson(content));
            log.info("Message sent: {}", contentsQueueName);
        } catch (Exception e) {
            log.error("Error sending message", e);
        }
    }

}

