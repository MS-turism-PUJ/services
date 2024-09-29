package com.turism.services.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
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
    private String description;
    private Double Latitude;
    private Double Longitude;
    private Date departureDate;
    private Date arrivalDate;
    private String transportType;
    @ManyToOne
    @JoinColumn(name = "serviceCategoryId")
    private ServiceCategory category;
    @OneToOne
    @JoinColumn(name = "contentId")
    private Content content;
}
