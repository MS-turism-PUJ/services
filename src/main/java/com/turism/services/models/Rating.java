package com.turism.services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;
    @Column(nullable = false)
    private Integer rating;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "serviceId")
    private Service service;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
