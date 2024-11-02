package com.turism.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDTO {
    private String message;

    private Object data = null;

    public ErrorDTO(String message) {
        this.message = message;
    }
}
