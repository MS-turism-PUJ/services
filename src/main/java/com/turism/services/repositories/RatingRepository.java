package com.turism.services.repositories;

import com.turism.services.models.Rating;
import com.turism.services.models.Service;
import com.turism.services.models.User;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating findByServiceAndUser(Service service, User user);
    List<Rating> findByService(Service service, Pageable pageable);
    Double findAverageRatingByService(Service service);
}
