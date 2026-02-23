package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
