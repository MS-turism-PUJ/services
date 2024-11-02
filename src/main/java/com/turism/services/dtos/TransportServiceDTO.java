package com.turism.services.dtos;

import java.time.LocalTime;
import java.util.Date;

import com.turism.services.models.Service;
import com.turism.services.models.ServiceCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TransportServiceDTO extends ServiceDTO {
    @NotNull(message = "Departure city is required")
    @Valid
    private PlaceDTO departure;

    @NotNull(message = "Arrival city is required")
    @Valid
    private PlaceDTO arrival;

    @NotNull(message = "Departure date is required")
    @Future(message = "Departure date must be in the future")
    private Date departureDate;

    @NotNull(message = "Time is required")
    private LocalTime time;

    @NotBlank(message = "Transport type is required")
    private String transportType;

    public Service toService() {
        Service service = new Service(
                name,
                price,
                description,
                city,
                country);
        service.setDepartureDate(departureDate);
        service.setTime(time);
        service.setTransportType(transportType);
        service.setLatitude(departure.getLatitude());
        service.setLongitude(departure.getLongitude());
        service.setArrivalLatitude(arrival.getLatitude());
        service.setArrivalLongitude(arrival.getLongitude());
        service.setCategory(ServiceCategory.TRANSPORT);

        return service;
    }
}
