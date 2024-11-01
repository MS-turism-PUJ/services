package com.turism.services.dtos;

import com.turism.services.models.Service;
import com.turism.services.models.ServiceCategory;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AlimentationServiceDTO extends ServiceDTO {
    @NotBlank(message = "Drink is required")
    private String drink;
    @NotBlank(message = "Lunch is required")
    private String lunch;
    @NotBlank(message = "Dessert is required")
    private String dessert;

    public Service toService() {
        Service service = new Service(
                name,
                price,
                description,
                city,
                country);
        service.setDrink(drink);
        service.setLunch(lunch);
        service.setDessert(dessert);
        service.setCategory(ServiceCategory.ALIMENTATION);

        return service;
    }
}
