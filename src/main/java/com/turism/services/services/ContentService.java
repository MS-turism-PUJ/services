package com.turism.services.services;

import com.turism.services.models.Content;
import com.turism.services.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public List<Content> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return contentRepository.findAll(pageable).getContent();
    }
}
