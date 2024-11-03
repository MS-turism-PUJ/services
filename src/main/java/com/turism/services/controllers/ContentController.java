package com.turism.services.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turism.services.dtos.ContentDTO;
import com.turism.services.dtos.ErrorDTO;
import com.turism.services.models.Content;
import com.turism.services.services.ContentService;
import com.turism.services.services.ServiceService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentSerivce;
    private final ServiceService serviceService;

    public ContentController(ContentService contentService, ServiceService serviceService) {
        this.contentSerivce = contentService;
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<Content> getAllMyContents(@RequestHeader("X-Preferred-Username") String username,
            @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        log.info("GET /contents with page: {} and limit: {} for user: {}", page, limit, username);
        return contentSerivce.getAllMyContents(username, page, limit);
    }

    @PostMapping
    public ResponseEntity<?> createContent(@RequestHeader("X-Preferred-Username") String username, @Valid @RequestBody ContentDTO contentDTO) {
        log.info("POST /contents for user: {}", username);
        if (!serviceService.existsService(contentDTO.getServiceId())) {
            return ResponseEntity.status(404).body(new ErrorDTO("Service not found", contentDTO.getServiceId()));
        }
        return ResponseEntity.ok(contentSerivce.createContent(contentDTO.toContent(), username));
    }
}
