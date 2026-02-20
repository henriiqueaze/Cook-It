package com.p5Project.cookIt.repositories;

import com.p5Project.cookIt.models.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
