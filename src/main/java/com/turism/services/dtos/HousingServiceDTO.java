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
public class HousingServiceDTO extends ServiceDTO {
    @NotBlank(message = "Housing type is required")
    @Valid
    private PlaceDTO place;
    @NotBlank(message = "Time is required")
    private Date time;

    public Service toService() {
        Service service = new Service(
            name,
            price,
            description,
            city,
            country
        );
        service.setLatitude(place.getLatitude());
        service.setLongitude(place.getLongitude());
        service.setTime(time);
        return service;
    }
}
