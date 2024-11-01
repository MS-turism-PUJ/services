package com.turism.services.dtos;

import java.io.Serializable;

import com.turism.services.models.Service;
import com.turism.services.models.ServiceCategory;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMessageDTO implements Serializable {
    private Float price;

    private String name;

    private String city;

    private String country;

    private String description;

    private Double latitude;

    private Double longitude;

    private Double arrivalLatitude;

    private Double arrivalLongitude;

    private String departureDate;

    private String time;

    private String transportType;

    private String drink;

    private String lunch;

    private String dessert;

    @Enumerated(EnumType.STRING)
    private ServiceCategory category;

    private String userId;

    public ServiceMessageDTO(Service service) {
        this.price = service.getPrice();
        this.name = service.getName();
        this.city = service.getCity();
        this.country = service.getCountry();
        this.description = service.getDescription();
        this.latitude = service.getLatitude();
        this.longitude = service.getLongitude();
        this.arrivalLatitude = service.getArrivalLatitude();
        this.arrivalLongitude = service.getArrivalLongitude();
        this.departureDate = service.getDepartureDate() == null ? null : service.getDepartureDate().toString();
        this.time = service.getTime() == null ? null : service.getTime().toString();
        this.transportType = service.getTransportType();
        this.drink = service.getDrink();
        this.lunch = service.getLunch();
        this.dessert = service.getDessert();
        this.category = service.getCategory();
        this.userId = service.getUser().getUserId();
    }
}
