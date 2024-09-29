package com.turism.services.services;

import com.turism.services.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
    private final ContentRepository contentRepository;

    @Autowired
    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Boolean existsContent(String contentId) {
        return contentRepository.existsById(contentId);
    }
}
