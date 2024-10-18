package com.turism.services.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AlimentationServiceDTO extends ServiceDTO {
    @NotBlank(message = "Drink is required")
    private String drink;
    @NotBlank(message = "Lunch is required")
    private String lunch;
    @NotBlank(message = "Dessert is required")
    private String dessert;
}
