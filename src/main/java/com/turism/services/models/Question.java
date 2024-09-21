package com.turism.services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String questionId;
    private String question;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
