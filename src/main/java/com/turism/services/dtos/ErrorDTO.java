package com.turism.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDTO {
    private String message;
    private String path;
    private Integer status;
}
