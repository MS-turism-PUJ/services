package com.turism.services.dtos;

import java.time.LocalTime;
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
public class HousingServiceDTO extends ServiceDTO {
    @NotNull(message = "Housing type is required")
    @Valid
    private PlaceDTO place;

    @NotNull(message = "Date is required")
    @Future(message = "Date must be in the future")
    private Date date;

    @NotNull(message = "Time is required")
    private LocalTime time;

    public Service toService() {
        Service service = new Service(
                name,
                price,
                description,
                city,
                country);
        service.setLatitude(place.getLatitude());
        service.setLongitude(place.getLongitude());
        service.setTime(time);
        service.setDepartureDate(date);
        service.setCategory(ServiceCategory.HOUSING);

        return service;
    }
}
