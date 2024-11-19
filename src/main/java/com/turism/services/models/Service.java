package com.turism.services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turism.services.dtos.ServiceMessageDTO;

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

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private Double arrivalLatitude;

    @Column
    private Double arrivalLongitude;

    @Column
    private Date departureDate;

    @Column
    private Integer duration;

    @Column
    private String transportType;

    @Column
    private String drink;

    @Column
    private String lunch;

    @Column
    private String dessert;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceCategory category;

    @OneToMany(mappedBy = "service")
    @JsonIgnore
    private List<Content> contents;

    @JoinColumn(name = "userId")
    @ManyToOne
    @JsonIgnore
    private User user;

    public Service(String serviceId) {
        this.serviceId = serviceId;
    }

    public Service(String name, Float price, String description, String city, String country) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.city = city;
        this.country = country;
    }

    public ServiceMessageDTO toServiceMessageDTO() {
        return new ServiceMessageDTO(this);
    }
}
