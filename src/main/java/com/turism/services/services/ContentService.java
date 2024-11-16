package com.turism.services.services;

import com.turism.services.dtos.ContentMessageDTO;
import com.turism.services.models.Content;
import com.turism.services.models.User;
import com.turism.services.repositories.ContentRepository;
import com.turism.services.repositories.UserRepository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final MessageQueueService messageQueueService;

    public ContentService(ContentRepository contentRepository, UserRepository userRepository, MessageQueueService messageQueueService) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.messageQueueService = messageQueueService;
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

    public Content getContent(String contentId) {
        return contentRepository.findById(contentId).orElse(null);
    }

    public Content createContent(Content content, String username, Boolean setPhoto) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Error("User not found");
        }

        content.setUser(user);
        content = publishContent(content);
        if (setPhoto) {
            content.setPhoto(content.getContentId());
            content = publishContent(content);
        }
        messageQueueService.sendContentMessage(new ContentMessageDTO(content));

        return content;
    }
}
