package com.turism.services.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    private String photo;
    @Column(nullable = false)
    private String type;

    public User(String userId) {
        this.userId = userId;
    }

    public User(String name, String email, String photo, String type) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.type = type;
    }
}
