package com.turism.services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "content_categories")
public class ContentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentCategoryId;
    @Column(nullable = false)
    private String type;
}
