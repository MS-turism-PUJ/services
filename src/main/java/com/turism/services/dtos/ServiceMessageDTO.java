package com.turism.services.dtos;

import java.io.Serializable;
import java.text.SimpleDateFormat;

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
    private String serviceId;

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

    private String duration;

    private String transportType;

    private String drink;

    private String lunch;

    private String dessert;

    @Enumerated(EnumType.STRING)
    private ServiceCategory category;

    private String userId;

    public ServiceMessageDTO(Service service) {
        this.serviceId = service.getServiceId();
        this.price = service.getPrice();
        this.name = service.getName();
        this.city = service.getCity();
        this.country = service.getCountry();
        this.description = service.getDescription();
        this.latitude = service.getLatitude();
        this.longitude = service.getLongitude();
        this.arrivalLatitude = service.getArrivalLatitude();
        this.arrivalLongitude = service.getArrivalLongitude();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.departureDate = service.getDepartureDate() == null ? null : sdf.format(service.getDepartureDate());
        this.duration = service.getDuration() == null ? null : service.getDuration().toString();
        this.transportType = service.getTransportType();
        this.drink = service.getDrink();
        this.lunch = service.getLunch();
        this.dessert = service.getDessert();
        this.category = service.getCategory();
        this.userId = service.getUser().getUserId();
    }
}
