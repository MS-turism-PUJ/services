package com.turism.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationErrorDTO {
    private String field;
    private String message;
}

