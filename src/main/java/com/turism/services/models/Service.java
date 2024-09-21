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
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String serviceId;
    private Float price;
    private String name;
    private String description = "";
    @Nullable
    private Double Latitude = null;
    @Nullable
    private Double Longitude = null;
    @Nullable
    private Date departureDate = null;
    @Nullable
    private Date arrivalDate = null;
    private String transportType = "";
    @ManyToOne
    @JoinColumn(name = "service_category_id")
    private ServiceCategory category;
    @OneToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
