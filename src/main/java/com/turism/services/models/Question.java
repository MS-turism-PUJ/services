package com.turism.services.models;

import jakarta.persistence.*;
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
    @JoinColumn(name = "contentId")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Question(String question, Content content, User user) {
        this.question = question;
        this.content = content;
        this.user = user;
    }
}
