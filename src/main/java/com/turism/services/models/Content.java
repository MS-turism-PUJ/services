package com.turism.services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String contentId;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String link;

    @Column
    private String photo;

    @Column
    private String photoExtension;

    @ManyToOne
    @JoinColumn(name = "serviceId")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Content(String contentId) {
        this.contentId = contentId;
    }
}
