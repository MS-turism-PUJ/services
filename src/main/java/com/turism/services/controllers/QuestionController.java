package com.turism.services.controllers;

import com.turism.services.dtos.ErrorDTO;
import com.turism.services.dtos.QuestionDTO;
import com.turism.services.services.ContentService;
import com.turism.services.services.QuestionService;
import com.turism.services.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;
    private final ContentService contentService;

    @Autowired
    public QuestionController(QuestionService questionService, UserService userService, ContentService contentService) {
        this.questionService = questionService;
        this.userService = userService;
        this.contentService = contentService;
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<?> getAllQuestionsByContent(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @PathVariable String contentId) {
        if (!contentService.existsContent(contentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Current content does not exist"));
        }

        return ResponseEntity.ok(questionService.getAllQuestionsByContent(page, size, contentId));
    }

    @PostMapping("/{contentId}")
    public ResponseEntity<?> createQuestion(@RequestHeader("X-Preferred-Username") String preferredUsername, @PathVariable String contentId, @RequestBody QuestionDTO questionDTO) {
        if (!contentService.existsContent(contentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Current content does not exist"));
        }

        String userId = userService.getUserByUsername(preferredUsername).getUserId();
        questionService.createQuestion(questionDTO.getQuestion(), contentId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
