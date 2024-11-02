package com.turism.services.dtos;

import com.turism.services.models.Content;
import com.turism.services.models.Service;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String image;

    private String link;

    private String serviceId;

    public Content toContent() {
        return new Content(null, name, description, image, link, new Service(serviceId), null);
    }
}
