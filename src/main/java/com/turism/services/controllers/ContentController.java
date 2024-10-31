package com.turism.services.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turism.services.models.Content;
import com.turism.services.services.ContentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentSerivce;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentSerivce = contentService;
    }

    @GetMapping
    public List<Content> getAllMyContents(@RequestHeader("X-Preferred-Username") String username,
            @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        log.info("GET /contents with page: {} and limit: {} for user: {}", page, limit, username);
        return contentSerivce.getAllMyContents(username, page, limit);
    }

    @PostMapping
    public Content createContent(@RequestHeader("X-Preferred-Username") String username) {
        return null;
    }
}
