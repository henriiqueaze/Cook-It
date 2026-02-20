package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.RecipeTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeTagRepository extends JpaRepository<RecipeTag, UUID> {
}
