package com.turism.services.dtos;

import com.turism.services.models.Service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    @NotBlank(message = "Name is required")
    protected String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    protected Float price;

    @NotBlank(message = "Description is required")
    protected String description;

    @NotBlank(message = "City is required")
    protected String city;

    @NotBlank(message = "Country is required")
    protected String country;

    public Service toService() {
        return new Service(
            name,
            price,
            description,
            city,
            country
        );
    }
}
