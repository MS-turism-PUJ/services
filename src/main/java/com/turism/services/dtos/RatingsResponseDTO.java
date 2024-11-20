package com.turism.services.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingsResponseDTO {
    private Double averageRating;
    private Long quantity;
    private List<RatingDTO> ratings;
}
