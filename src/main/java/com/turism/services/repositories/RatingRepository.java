package com.turism.services.repositories;

import com.turism.services.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends JpaRepository<Rating, String> {
}
