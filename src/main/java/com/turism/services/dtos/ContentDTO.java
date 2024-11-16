package com.turism.services.dtos;

import com.turism.services.models.Content;
import com.turism.services.models.Service;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String link;

    private String serviceId;

    private MultipartFile photo = null;

    public Content toContent() {
        return new Content(null, name, description, link, null, photo != null ? FilenameUtils.getExtension(photo.getOriginalFilename()) : null, new Service(serviceId), null);
    }
}
