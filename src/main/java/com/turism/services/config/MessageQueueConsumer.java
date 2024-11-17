package com.turism.services.config;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.turism.services.services.UserService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.turism.services.dtos.UserMessageDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MessageQueueConsumer {
    private final String queueName = "usersQueue";

    private final String groupId = "services-group";

    private final UserService userService;

    @Autowired
    public MessageQueueConsumer(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public ConsumerFactory<String, UserMessageDTO> consumerFactory(@Value("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put("auto.offset.reset", "earliest");
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.turism.*");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, UserMessageDTO>> kafkaListenerContainerFactory(@Value("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers) {
        ConcurrentKafkaListenerContainerFactory<String, UserMessageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(kafkaBootstrapServers));
        return factory;
    }

    @KafkaListener(topics = queueName, groupId = groupId)
    public void listen(String userJson) {
        log.info("Received UserMessageDTO: {}", userJson);
        Gson gson = new Gson();
        UserMessageDTO user = gson.fromJson(userJson, UserMessageDTO.class);
        userService.createUser(user.toUser());
    }
}