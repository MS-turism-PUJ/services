package com.turism.services.controllers;

import org.junit.jupiter.api.TestMethodOrder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.turism.services.dtos.ContentDTO;
import com.turism.services.models.Content;
import com.turism.services.models.Service;
import com.turism.services.models.ServiceCategory;
import com.turism.services.models.User;
import com.turism.services.repositories.ContentRepository;
import com.turism.services.repositories.ServiceRepository;
import com.turism.services.repositories.UserRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public ContentRepository contentRepository;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @Container
    static final MinIOContainer minio = new MinIOContainer("minio/minio:RELEASE.2024-11-07T00-52-20Z.fips");

    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("minio.url", minio::getS3URL);
        registry.add("minio.access.key", minio::getUserName);
        registry.add("minio.access.secret", minio::getPassword);

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    static KafkaConsumer<Object, Object> mockKafkaConsumer;

    static void createMockKafkaConsumer() {
        String groupId = "test-group";
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.turism.*");
        mockKafkaConsumer = new KafkaConsumer<>(properties, new JsonDeserializer<>(), new JsonDeserializer<>());
        mockKafkaConsumer.subscribe(List.of("contentsQueue"));
    }

    static final User mockProvider = new User("userId", "providerUsernameTest", "providerNameTest",
            "providerEmail@test.com");

    static Service mockService = new Service(
            "",
            10.0f,
            "alimentationNameTest",
            "alimentationCityTest",
            "alimentationCountryTest",
            "alimentationDescriptionTest",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "alimentationDrinkTest",
            "alimentationLunchTest",
            "alimentationDessertTest",
            null,
            null,
            mockProvider);

    static final MockMultipartFile mockPhotoJPG = new MockMultipartFile(
            "photo",
            "profile-picture.jpg",
            "image/jpeg",
            "Fake image content".getBytes());

    @BeforeAll
    static void beforeAll(@Autowired UserRepository userRepository, @Autowired ServiceRepository serviceRepository) {
        postgres.start();
        kafka.start();
        createMockKafkaConsumer();
        userRepository.save(mockProvider);
        mockService.setCategory(ServiceCategory.ALIMENTATION);
        mockService = serviceRepository.save(mockService);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        kafka.stop();
    }

    @Test
    @Order(1)
    void createContent() throws Exception {
        ContentDTO contentDTO = new ContentDTO(
                "contentNameTest",
                "contentDescriptionTest",
                "contentLinkTest",
                mockService.getServiceId(),
                mockPhotoJPG);

        mockMvc.perform(multipart("/contents")
                .file(mockPhotoJPG)
                .param("name", contentDTO.getName())
                .param("description", contentDTO.getDescription())
                .param("link", contentDTO.getLink())
                .param("serviceId", contentDTO.getServiceId())
                .header("X-Preferred-Username", "providerUsernameTest"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contentId").isString())
                .andExpect(jsonPath("$.name").value(contentDTO.getName()))
                .andExpect(jsonPath("$.description").value(contentDTO.getDescription()))
                .andExpect(jsonPath("$.link").value(contentDTO.getLink()))
                .andExpect(jsonPath("$.service").isMap())
                .andExpect(jsonPath("$.photo").isString());
    }

    @Test
    @Order(2)
    void getAllMyContents() throws Exception {
        mockMvc.perform(get("/contents")
                .header("X-Preferred-Username", "providerUsernameTest")
                .param("page", "1")
                .param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(2)
    void getPhoto() throws Exception {
        Content content = contentRepository.findAll().get(0);

        mockMvc.perform(get("/contents/" + content.getContentId() + "/photo")
                .header("X-Preferred-Username", "providerUsernameTest")
                .param("contentId", "contentIdTest"))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
