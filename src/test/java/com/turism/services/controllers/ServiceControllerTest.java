package com.turism.services.controllers;

import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.google.gson.Gson;
import com.turism.services.dtos.AlimentationServiceDTO;
import com.turism.services.dtos.EcoWalkServiceDTO;
import com.turism.services.dtos.HousingServiceDTO;
import com.turism.services.dtos.PlaceDTO;
import com.turism.services.dtos.TransportServiceDTO;
import com.turism.services.models.Service;
import com.turism.services.models.User;
import com.turism.services.repositories.ServiceRepository;
import com.turism.services.repositories.UserRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServiceRepository serviceRepository;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

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
        mockKafkaConsumer.subscribe(List.of("servicesQueue"));
    }

    static final User mockProvider = new User("userId", "providerUsernameTest", "providerNameTest",
            "providerEmail@test.com");

    @BeforeAll
    static void beforeAll(@Autowired UserRepository userRepository) {
        postgres.start();
        kafka.start();
        createMockKafkaConsumer();
        userRepository.save(mockProvider);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        kafka.stop();
    }

    @Test
    @Order(1)
    void createAlimentationService() throws Exception {
        AlimentationServiceDTO alimentationServiceDTO = new AlimentationServiceDTO(
                "alimentationNameTest",
                10.0f,
                "alimentationDescriptionTest",
                "alimentationCityTest",
                "alimentationCountryTest",
                "alimentationDrinkTest",
                "alimentationLunchTest",
                "alimentationDessertTest");

        Gson gson = new Gson();

        mockMvc.perform(post("/alimentation")
                .header("X-Preferred-Username", mockProvider.getUsername())
                .contentType("application/json")
                .content(gson.toJson(alimentationServiceDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").exists())
                .andExpect(jsonPath("$.name").value(alimentationServiceDTO.getName()))
                .andExpect(jsonPath("$.description").value(alimentationServiceDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(alimentationServiceDTO.getPrice()))
                .andExpect(jsonPath("$.city").value(alimentationServiceDTO.getCity()))
                .andExpect(jsonPath("$.country").value(alimentationServiceDTO.getCountry()))
                .andExpect(jsonPath("$.drink").value(alimentationServiceDTO.getDrink()))
                .andExpect(jsonPath("$.lunch").value(alimentationServiceDTO.getLunch()))
                .andExpect(jsonPath("$.dessert").value(alimentationServiceDTO.getDessert()));
    }

    @Test
    @Order(1)
    void createHousingService() throws Exception {
        PlaceDTO place = new PlaceDTO(10.0, 10.0);
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        HousingServiceDTO housingServiceDTO = new HousingServiceDTO(
                "housingNameTest",
                10.0f,
                "housingDescriptionTest",
                "housingCityTest",
                "housingCountryTest",
                place,
                tomorrow,
                Duration.parse("P9D"));

        Gson gson = new Gson();

        mockMvc.perform(post("/housing")
                .header("X-Preferred-Username", mockProvider.getUsername())
                .contentType("application/json")
                .content(gson.toJson(housingServiceDTO.toMap())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").exists())
                .andExpect(jsonPath("$.name").value(housingServiceDTO.getName()))
                .andExpect(jsonPath("$.description").value(housingServiceDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(housingServiceDTO.getPrice()))
                .andExpect(jsonPath("$.city").value(housingServiceDTO.getCity()))
                .andExpect(jsonPath("$.country").value(housingServiceDTO.getCountry()))
                .andExpect(jsonPath("$.latitude").value(housingServiceDTO.getPlace().getLatitude()))
                .andExpect(jsonPath("$.longitude").value(housingServiceDTO.getPlace().getLongitude()))
                .andExpect(jsonPath("$.duration").value(housingServiceDTO.getDuration().toString()));
    }

    @Test
    @Order(1)
    void createEcoWalkService() throws Exception {
        PlaceDTO departure = new PlaceDTO(10.0, 10.0);
        PlaceDTO arrival = new PlaceDTO(20.0, 20.0);
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        EcoWalkServiceDTO ecoWalkServiceDTO = new EcoWalkServiceDTO(
                "ecoWalkNameTest",
                10.0f,
                "ecoWalkDescriptionTest",
                "ecoWalkCityTest",
                "ecoWalkCountryTest",
                departure,
                arrival,
                tomorrow);

        Gson gson = new Gson();

        mockMvc.perform(post("/ecowalk")
                .header("X-Preferred-Username", mockProvider.getUsername())
                .contentType("application/json")
                .content(gson.toJson(ecoWalkServiceDTO.toMap())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").exists())
                .andExpect(jsonPath("$.name").value(ecoWalkServiceDTO.getName()))
                .andExpect(jsonPath("$.description").value(ecoWalkServiceDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(ecoWalkServiceDTO.getPrice()))
                .andExpect(jsonPath("$.city").value(ecoWalkServiceDTO.getCity()))
                .andExpect(jsonPath("$.country").value(ecoWalkServiceDTO.getCountry()))
                .andExpect(jsonPath("$.latitude").value(ecoWalkServiceDTO.getDeparture().getLatitude()))
                .andExpect(jsonPath("$.longitude").value(ecoWalkServiceDTO.getDeparture().getLongitude()))
                .andExpect(jsonPath("$.arrivalLatitude").value(ecoWalkServiceDTO.getArrival().getLatitude()))
                .andExpect(jsonPath("$.arrivalLongitude").value(ecoWalkServiceDTO.getArrival().getLongitude()));
    }

    @Test
    @Order(1)
    void createTransportService() throws Exception {
        PlaceDTO departure = new PlaceDTO(10.0, 10.0);
        PlaceDTO arrival = new PlaceDTO(20.0, 20.0);
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        TransportServiceDTO transportServiceDTO = new TransportServiceDTO(
                "transportNameTest",
                10.0f,
                "transportDescriptionTest",
                "transportCityTest",
                "transportCountryTest",
                departure,
                arrival,
                tomorrow,
                Duration.parse("P9D"),
                "transportTypeTest");
        
        Gson gson = new Gson();

        mockMvc.perform(post("/transport")
                .header("X-Preferred-Username", mockProvider.getUsername())
                .contentType("application/json")
                .content(gson.toJson(transportServiceDTO.toMap())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").exists())
                .andExpect(jsonPath("$.name").value(transportServiceDTO.getName()))
                .andExpect(jsonPath("$.description").value(transportServiceDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(transportServiceDTO.getPrice()))
                .andExpect(jsonPath("$.city").value(transportServiceDTO.getCity()))
                .andExpect(jsonPath("$.country").value(transportServiceDTO.getCountry()))
                .andExpect(jsonPath("$.latitude").value(transportServiceDTO.getDeparture().getLatitude()))
                .andExpect(jsonPath("$.longitude").value(transportServiceDTO.getDeparture().getLongitude()))
                .andExpect(jsonPath("$.arrivalLatitude").value(transportServiceDTO.getArrival().getLatitude()))
                .andExpect(jsonPath("$.arrivalLongitude").value(transportServiceDTO.getArrival().getLongitude()))
                .andExpect(jsonPath("$.duration").value(transportServiceDTO.getDuration().toString()))
                .andExpect(jsonPath("$.transportType").value(transportServiceDTO.getTransportType()));
    }

    @Test
    @Order(2)
    void getServicesTest() throws Exception {
        mockMvc.perform(get("/")
                .header("X-Preferred-Username", mockProvider.getUsername())
                .param("page", "1")
                .param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(2)
    void userMessagesSentTest() throws Exception {
        ConsumerRecords<Object, Object> records = mockKafkaConsumer.poll(Duration.ofSeconds(5));

        assertEquals(4, records.count());

        List<String> actualMessages = new ArrayList<>();
        for (ConsumerRecord<Object, Object> record : records) {
            actualMessages.add(record.value().toString());
        }

        Gson gson = new Gson();

        List<Service> services = serviceRepository.findAll();
        List<String> messages = services.stream().map(service -> gson.toJson(service.toServiceMessageDTO())).toList();

        assertTrue(actualMessages.containsAll(messages));
    }
}
