package com.turism.services.dtos;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public EcoWalkServiceDTO(String name, Float price, String description, String city, String country, PlaceDTO departure, PlaceDTO arrival, Date date) {
        super(name, price, description, city, country);
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
    }

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
        map.put("date", date.toInstant().toString());
        return map;
    }
}
