package com.turism.services.controllers;

import com.turism.services.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping("/all")
    public String getAllContents(@RequestHeader("X-Preferred-Username") String preferredUsername, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        System.out.println("User: " + preferredUsername);
        return contentService.findAll(page, size).toString();
    }
}
