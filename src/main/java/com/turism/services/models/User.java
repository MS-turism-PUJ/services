package com.turism.services.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String userId;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private String photo;

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, String username, String name, String email, String photo) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.photo = photo;
    }
}
