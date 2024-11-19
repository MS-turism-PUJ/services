package com.turism.services.dtos;

import java.time.Duration;
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
public class HousingServiceDTO extends ServiceDTO {
    @NotNull(message = "Place is required")
    @Valid
    private PlaceDTO place;

    @NotNull(message = "Date is required")
    @Future(message = "Date must be in the future")
    private Date date;

    @NotNull(message = "Duration is required")
    private Integer duration;

    public HousingServiceDTO(String name, Float price, String description, String city, String country, PlaceDTO place,
            Date date, Integer duration) {
        super(name, price, description, city, country);
        this.place = place;
        this.date = date;
        this.duration = duration;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("price", price);
        map.put("description", description);
        map.put("city", city);
        map.put("country", country);
        Map<String, Object> place = new HashMap<>();
        place.put("latitude", this.place.getLatitude());
        place.put("longitude", this.place.getLongitude());
        map.put("place", place);
        map.put("date", date.toInstant().toString());
        map.put("duration", duration.toString());
        return map;
    }

    public Service toService() {
        Service service = new Service(
                name,
                price,
                description,
                city,
                country);
        service.setLatitude(place.getLatitude());
        service.setLongitude(place.getLongitude());
        service.setDuration(duration);
        service.setDepartureDate(date);
        service.setCategory(ServiceCategory.HOUSING);

        return service;
    }
}
