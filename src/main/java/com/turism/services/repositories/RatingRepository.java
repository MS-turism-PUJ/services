package com.turism.services.repositories;

import com.turism.services.models.Rating;
import com.turism.services.models.Service;
import com.turism.services.models.User;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, String> {
    Rating findByServiceAndUser(Service service, User user);
    List<Rating> findByService(Service service, Pageable pageable);
    @Query("SELECT AVG(CAST(r.rating AS double)) FROM Rating r WHERE r.service = :service")
    Double findAverageRatingByService(@Param("service") Service service);
}
