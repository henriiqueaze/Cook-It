package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.PantryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PantryItemRepository extends JpaRepository<PantryItem, UUID> {
}
