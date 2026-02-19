package com.p5Project.Cook_It.repositories;

import com.p5Project.Cook_It.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TagRepository extends JpaRepository<User, UUID> {
}
