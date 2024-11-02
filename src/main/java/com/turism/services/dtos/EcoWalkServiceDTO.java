package com.turism.services.dtos;

import java.util.Date;

import com.turism.services.models.Service;
import com.turism.services.models.ServiceCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EcoWalkServiceDTO extends ServiceDTO {
    @NotNull(message = "Departure is required")
    @Valid
    private PlaceDTO departure;

    @NotNull(message = "Arrival is required")
    @Valid
    private PlaceDTO arrival;

    @NotNull(message = "Date is required")
    @Future(message = "Date must be in the future")
    private Date date;

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
