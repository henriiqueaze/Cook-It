package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
}
