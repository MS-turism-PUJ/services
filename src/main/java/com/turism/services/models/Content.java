package com.turism.services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String contentId;
    @Column(nullable = false)
    private String name;
    private String description;
    private String image;
    private String link;
    @ManyToOne
    @JoinColumn(name = "content_category_id")
    private ContentCategory category;
    @OneToOne
    @JoinColumn(name = "service_id")
    private Service service;

    public Content(String contentId) {
        this.contentId = contentId;
    }
}
