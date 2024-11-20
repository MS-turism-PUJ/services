package com.turism.services.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.turism.services.models.Rating;
import com.turism.services.models.Service;
import com.turism.services.models.User;
import com.turism.services.repositories.RatingRepository;
import com.turism.services.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.stereotype.Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    public List<Rating> getServiceRatings(Service service, Integer page, Integer limit) {
        log.info("Getting ratings for service {}", service.getServiceId());

        Pageable pageable = PageRequest.of(page - 1, limit);

        return ratingRepository.findByService(service, pageable);
    }

    public Double getAverageRating(Service service) {
        return ratingRepository.findAverageRatingByService(service);
    }

    public Long getQuantity(Service service) {
        return ratingRepository.countByService(service);
    }

    public Rating rateService(String username, Service service, Integer rating, String comment) {
        log.info("Rating service with id: {} for user: {} with rating: {}", service.getServiceId(), username, rating);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Error("User not found");
        }

        Rating previousRating = ratingRepository.findByServiceAndUser(service, user);

        if (previousRating != null) {
            previousRating.setRating(rating);
            previousRating.setComment(comment);
            return ratingRepository.save(previousRating);
        }

        return ratingRepository.save(new Rating(null, rating, comment, service, user));
    }

    public Rating getRatingByUser(String username, Service service) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Error("User not found");
        }

        return ratingRepository.findByServiceAndUser(service, user);
    }
}
