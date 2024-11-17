package com.turism.services.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turism.services.dtos.ErrorDTO;
import com.turism.services.dtos.RatingDTO;
import com.turism.services.dtos.RatingsResponseDTO;
import com.turism.services.models.Rating;
import com.turism.services.models.Service;
import com.turism.services.services.RatingService;
import com.turism.services.services.ServiceService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ratings")
public class RatingController {
    private final ServiceService serviceService;
    private final RatingService ratingService;

    public RatingController(ServiceService serviceService, RatingService ratingService) {
        this.serviceService = serviceService;
        this.ratingService = ratingService;
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<?> getServiceRatings(@RequestHeader("X-Preferred-Username") String username,
            @PathVariable String serviceId, @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        log.info("Getting ratings for service {}", serviceId);
        Service service = serviceService.getService(serviceId);

        if (service == null) {
            return ResponseEntity.status(404).body(new ErrorDTO("Service " + serviceId + " not found"));
        }

        List<Rating> ratings = ratingService.getServiceRatings(username, service, page, limit);

        Double averageRating = ratingService.getAverageRating(service);

        RatingsResponseDTO ratingsResponseDTO = new RatingsResponseDTO(averageRating, ratings.stream().map(rating -> {
            return new RatingDTO(rating.getRating(), rating.getComment());
        }).toList());

        return ResponseEntity.ok(ratingsResponseDTO);
    }

    @PostMapping("/rate/{serviceId}")
    public ResponseEntity<?> rateService(@RequestHeader("X-Preferred-Username") String username,
            @PathVariable String serviceId, @Valid @RequestBody RatingDTO ratingDTO) {
        log.info("Rating service");
        Service service = serviceService.getService(serviceId);

        if (service == null) {
            return ResponseEntity.status(404).body(new ErrorDTO("Service " + serviceId + " not found"));
        }

        return ResponseEntity
                .ok(ratingService.rateService(username, service, ratingDTO.getRating(), ratingDTO.getComment()));
    }
}
