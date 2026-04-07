package com.p5Project.cookIt.repository;

import com.p5Project.cookIt.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findByRecipeId(String recipeId);

    List<Comment> findByUserId(String userId);
}