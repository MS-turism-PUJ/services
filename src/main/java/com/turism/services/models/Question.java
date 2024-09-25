package com.turism.services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String questionId;
    @Column(nullable = false)
    private String question;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Question(String question, Content content, User user) {
        this.question = question;
        this.content = content;
        this.user = user;
    }
}
