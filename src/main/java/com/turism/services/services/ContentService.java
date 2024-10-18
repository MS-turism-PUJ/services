package com.turism.services.services;

import com.turism.services.models.Content;
import com.turism.services.models.User;
import com.turism.services.repositories.ContentRepository;
import com.turism.services.repositories.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContentService(ContentRepository contentRepository, UserRepository userRepository) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
    }

    public List<Content> getAllMyContents(String username, Integer page, Integer limit) {
        User user = userRepository.findByUsername(username);
        Pageable pageable = PageRequest.of(page - 1, limit);
        return contentRepository.findAllByUser(user, pageable).getContent();
    }

    public Content publishContent(Content content) {
        return contentRepository.save(content);
    }

    public Boolean existsContent(String contentId) {
        return contentRepository.existsById(contentId);
    }
}
