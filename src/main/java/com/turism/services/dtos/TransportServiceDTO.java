package com.turism.services.dtos;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @NotNull(message = "Departure is required")
    @Valid
    private PlaceDTO departure;

    @NotNull(message = "Arrival is required")
    @Valid
    private PlaceDTO arrival;

    @NotNull(message = "Departure date is required")
    @Future(message = "Departure date must be in the future")
    private Date departureDate;

    @NotNull(message = "Duration is required")
    private Integer duration;

    @NotBlank(message = "Transport type is required")
    private String transportType;

    public TransportServiceDTO(String name, Float price, String description, String city, String country,
            PlaceDTO departure, PlaceDTO arrival, Date departureDate, Integer duration, String transportType) {
        super(name, price, description, city, country);
        this.departure = departure;
        this.arrival = arrival;
        this.departureDate = departureDate;
        this.duration = duration;
        this.transportType = transportType;
    }

    public Service toService() {
        Service service = new Service(
                name,
                price,
                description,
                city,
                country);
        service.setDepartureDate(departureDate);
        service.setTransportType(transportType);
        service.setLatitude(departure.getLatitude());
        service.setLongitude(departure.getLongitude());
        service.setArrivalLatitude(arrival.getLatitude());
        service.setArrivalLongitude(arrival.getLongitude());
        service.setCategory(ServiceCategory.TRANSPORT);

        return service;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("price", price);
        map.put("description", description);
        map.put("city", city);
        map.put("country", country);
        Map<String, Object> departure = new HashMap<>();
        departure.put("latitude", this.departure.getLatitude());
        departure.put("longitude", this.departure.getLongitude());
        map.put("departure", departure);
        Map<String, Object> arrival = new HashMap<>();
        arrival.put("latitude", this.arrival.getLatitude());
        arrival.put("longitude", this.arrival.getLongitude());
        map.put("arrival", arrival);
        map.put("departureDate", departureDate.toInstant().toString());
        map.put("duration", duration.toString());
        map.put("transportType", transportType);
        return map;
    }
}
