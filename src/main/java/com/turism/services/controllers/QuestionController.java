package com.turism.services.controllers;

import com.turism.services.dtos.ErrorDTO;
import com.turism.services.dtos.QuestionDTO;
import com.turism.services.services.ContentService;
import com.turism.services.services.QuestionService;
import com.turism.services.services.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;
    private final ContentService contentService;

    public QuestionController(QuestionService questionService, UserService userService, ContentService contentService) {
        this.questionService = questionService;
        this.userService = userService;
        this.contentService = contentService;
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<?> getAllQuestionsByContent(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit, @PathVariable String contentId) {
        log.info("GET /questions/{} with page: {} and limit: {}", contentId, page, limit);
        if (!contentService.existsContent(contentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Current content does not exist"));
        }

        return ResponseEntity.ok(questionService.getAllQuestionsByContent(page, limit, contentId));
    }

    @PostMapping("/{contentId}")
    public ResponseEntity<?> createQuestion(@RequestHeader("X-Preferred-Username") String username,
            @PathVariable String contentId, @Valid @RequestBody QuestionDTO questionDTO) {
        log.info("POST /questions/{} with question: {} as user: {}", contentId, questionDTO.getQuestion(), username);
        if (!contentService.existsContent(contentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Current content does not exist"));
        }

        String userId = userService.getUserByUsername(username).getUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(questionDTO.getQuestion(), contentId, userId));
    }
}
