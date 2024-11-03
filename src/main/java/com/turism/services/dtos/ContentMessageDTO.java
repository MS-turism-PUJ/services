package com.turism.services.dtos;

import java.io.Serializable;

import com.turism.services.models.Content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentMessageDTO implements Serializable {
    private String contentId;

    private String name;

    private String description;

    private String link;

    private String serviceId;

    private String userId;

    public ContentMessageDTO(Content content) {
        this.contentId = content.getContentId();
        this.name = content.getName();
        this.description = content.getDescription();
        this.link = content.getLink();
        this.serviceId = content.getService().getServiceId();
        this.userId = content.getUser().getUserId();
    }
}
