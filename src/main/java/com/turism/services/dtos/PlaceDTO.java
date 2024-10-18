package com.turism.services.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceDTO {
    @NotBlank(message = "Latitude is required")
    private Double latitude;
    @NotBlank(message = "Longitude is required")
    private Double longitude;
}
