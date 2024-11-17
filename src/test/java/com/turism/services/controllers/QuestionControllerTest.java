package com.turism.services.controllers;

import org.junit.jupiter.api.TestMethodOrder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import com.turism.services.dtos.QuestionDTO;
import com.turism.services.models.Content;
import com.turism.services.models.User;
import com.turism.services.repositories.ContentRepository;
import com.turism.services.repositories.UserRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class QuestionControllerTest {

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

    static Content mockContent = new Content(null, "contentTitleTest", "contentDescriptionTest",
            "www.link.test.com", null, null, null, mockProvider);

    @BeforeAll
    static void beforeAll(@Autowired UserRepository userRepository, @Autowired ContentRepository contentRepository) {
        postgres.start();
        userRepository.save(mockProvider);
        mockContent = contentRepository.save(mockContent);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @Order(1)
    void createQuestion() throws Exception {
        QuestionDTO questionDTO = new QuestionDTO("questionTest");

        Gson gson = new Gson();

        mockMvc.perform(post("/questions/{contentId}", mockContent.getContentId())
                .header("X-Preferred-Username", mockProvider.getUsername())
                .contentType("application/json")
                .content(gson.toJson(questionDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.question").value(questionDTO.getQuestion()));
    }

    @Test
    @Order(2)
    void getAllQuestionsByContent() throws Exception {
        mockMvc.perform(get("/questions/" + mockContent.getContentId())
                .header("X-Preferred-Username", mockProvider.getUsername()))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
