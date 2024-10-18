package com.turism.services.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class EcoWalkServiceDTO extends ServiceDTO {
    @NotBlank(message = "Departure is required")
    @Valid
    private PlaceDTO departure;
    @NotBlank(message = "Arrival is required")
    @Valid
    private PlaceDTO arrival;
}
