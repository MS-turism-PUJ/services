package com.turism.services.controllers;

import org.junit.jupiter.api.TestMethodOrder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.google.gson.Gson;
import com.turism.services.dtos.RatingDTO;
import com.turism.services.models.Service;
import com.turism.services.models.ServiceCategory;
import com.turism.services.models.User;
import com.turism.services.repositories.ServiceRepository;
import com.turism.services.repositories.UserRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
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

    @BeforeAll
    static void beforeAll(@Autowired UserRepository userRepository, @Autowired ServiceRepository serviceRepository) {
        postgres.start();
        userRepository.save(mockProvider);
        mockService.setCategory(ServiceCategory.ALIMENTATION);
        mockService = serviceRepository.save(mockService);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @Order(1)
    void rateService() throws Exception {
        RatingDTO ratingDTO = new RatingDTO(
                5,
                "test comment");
        
        Gson gson = new Gson();

        mockMvc.perform(post("/ratings/rate/" + mockService.getServiceId())
                .header("X-Preferred-Username", mockProvider.getUsername())
                .contentType("application/json")
                .content(gson.toJson(ratingDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("test comment"));
    }

    @Test
    @Order(2)
    void getRatingsByService() throws Exception {
        mockMvc.perform(get("/ratings/" + mockService.getServiceId())
                .header("X-Preferred-Username", mockProvider.getUsername())
                .param("page", "1")
                .param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageRating").value(5.0))
                .andExpect(jsonPath("$.ratings").isArray());
    }

}
