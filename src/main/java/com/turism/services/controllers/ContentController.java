package com.turism.services.controllers;

import com.turism.services.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping("/content/all")
    public String getAllContents() {
        return contentService.findAll(1, 10).toString();
    }
}
