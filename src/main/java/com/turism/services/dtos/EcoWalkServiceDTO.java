package com.turism.services.dtos;

import com.turism.services.models.Service;
import com.turism.services.models.ServiceCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EcoWalkServiceDTO extends ServiceDTO {

    @NotBlank(message = "Departure is required")
    @Valid
    private PlaceDTO departure;

    @NotBlank(message = "Arrival is required")
    @Valid
    private PlaceDTO arrival;

    public Service toService() {
        Service service = new Service(
                name,
                price,
                description,
                city,
                country);
        service.setLatitude(departure.getLatitude());
        service.setLongitude(departure.getLongitude());
        service.setArrivalLatitude(arrival.getLatitude());
        service.setArrivalLongitude(arrival.getLongitude());
        service.setCategory(ServiceCategory.ECO_WALK);

        return service;
    }
}
