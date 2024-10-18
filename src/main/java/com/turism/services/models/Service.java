package com.turism.services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String serviceId;
    @Column(nullable = false)
    private Float price;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String description;
    private Double latitude;
    private Double longitude;
    private Double arrivalLatitude;
    private Double arrivalLongitude;
    private Date departureDate;
    private Date time;
    private String transportType;
    private String drink;
    private String lunch;
    private String dessert;
    @ManyToOne
    @JoinColumn(name = "serviceCategoryId")
    private ServiceCategory category;

    public Service(String name, Float price, String description, String city, String country) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.city = city;
        this.country = country;
    }
}
