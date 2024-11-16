package com.turism.services.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "ratings", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "serviceId", "userId" })
})
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ratingId;

    @Column(nullable = false)
    private Integer rating;

    @Column
    private String comment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "serviceId")
    private Service service;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
