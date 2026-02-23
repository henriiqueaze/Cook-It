package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
}
