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
    @OneToOne
    @JoinColumn(name = "serviceId")
    private Service service;
    @ManyToOne
    @JoinColumn(name = "contentCategoryId")
    private ContentCategory category;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Content(String contentId) {
        this.contentId = contentId;
    }
}
