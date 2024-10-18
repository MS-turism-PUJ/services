package com.turism.services.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionDTO {
    @NotBlank(message = "Question is required")
    private String question;
}
