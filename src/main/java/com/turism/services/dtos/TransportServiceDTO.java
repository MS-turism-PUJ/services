package com.turism.services.dtos;

import java.util.Date;

import com.turism.services.models.Service;

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
public class TransportServiceDTO extends ServiceDTO {
    @NotBlank(message = "Departure city is required")
    @Valid
    private PlaceDTO departure;
    @NotBlank(message = "Arrival city is required")
    @Valid
    private PlaceDTO arrival;
    @NotBlank(message = "Departure date is required")
    private Date departureDate;
    @NotBlank(message = "Time is required")
    private Date time;
    @NotBlank(message = "Transport type is required")
    private String transportType;

    public Service toService() {
        Service service = new Service(
            name,
            price,
            description,
            city,
            country
        );
        service.setDepartureDate(departureDate);
        service.setTime(time);
        service.setTransportType(transportType);
        service.setLatitude(departure.getLatitude());
        service.setLongitude(departure.getLongitude());
        service.setArrivalLatitude(arrival.getLatitude());
        service.setArrivalLongitude(arrival.getLongitude());

        return service;
    }
}
