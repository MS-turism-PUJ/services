package com.turism.services.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turism.services.dtos.ContentDTO;
import com.turism.services.dtos.ErrorDTO;
import com.turism.services.dtos.ValidationErrorDTO;
import com.turism.services.models.Content;
import com.turism.services.models.FileType;
import com.turism.services.services.ContentService;
import com.turism.services.services.MinioService;
import com.turism.services.services.ServiceService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentSerivce;
    private final ServiceService serviceService;
    private final MinioService minioService;

    public ContentController(ContentService contentService, ServiceService serviceService, MinioService minioService) {
        this.contentSerivce = contentService;
        this.serviceService = serviceService;
        this.minioService = minioService;
    }

    @GetMapping
    public List<Content> getAllMyContents(@RequestHeader("X-Preferred-Username") String username,
            @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        log.info("GET /contents with page: {} and limit: {} for user: {}", page, limit, username);
        return contentSerivce.getAllMyContents(username, page, limit);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> createContent(@RequestHeader("X-Preferred-Username") String username, @Valid @ModelAttribute ContentDTO contentDTO) {
        log.info("POST /contents for user: {}", username);
        if (!serviceService.existsService(contentDTO.getServiceId())) {
            return ResponseEntity.status(404).body(new ErrorDTO("Service not found", contentDTO.getServiceId()));
        }

        if (contentDTO.getPhoto() != null
                && !contentDTO.getPhoto().getContentType().equals("image/jpeg")
                && !contentDTO.getPhoto().getContentType().equals("image/png")
                && !contentDTO.getPhoto().getContentType().equals("image/svg+xml")
                && !contentDTO.getPhoto().getContentType().equals("image/webp")) {
            return ResponseEntity.badRequest()
                    .body(new ValidationErrorDTO("photo", "Photo extension not supported, only jpg, jpeg and png"));
        }

        Content content = contentSerivce.createContent(contentDTO.toContent(), username, contentDTO.getPhoto() != null);

        if (contentDTO.getPhoto() != null) {
            try {
                minioService.uploadFile(content.getPhoto(), contentDTO.getPhoto());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println(e);
                return ResponseEntity.badRequest().body(new ValidationErrorDTO("photo", "Error uploading photo"));
            }
        }

        return ResponseEntity.ok(contentSerivce.createContent(contentDTO.toContent(), username, contentDTO.getPhoto() != null));
    }

    @GetMapping("/{contentId}/photo")
    public ResponseEntity<?> getPhoto(@PathVariable String contentId) {
        log.info("GET /contents/photo for content: {}", contentId);

        Content content = contentSerivce.getContent(contentId);
        if (content == null) {
            return ResponseEntity.status(404).body(new ErrorDTO("Content not found", contentId));
        }

        if (content.getPhoto() == null) {
            return ResponseEntity.status(404).body(new ErrorDTO("Content has no photo", contentId));
        }

        try {
            return ResponseEntity.ok()
                    .contentType(FileType.fromFilename(content.getPhotoExtension()))
                    .body(IOUtils.toByteArray(minioService.getObject(content.getPhoto())));
        } catch (Exception e) {
            log.error("Error getting photo", e);
            return ResponseEntity.badRequest().body("Error getting photo");
        }
    }
}
