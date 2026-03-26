package com.p5Project.cookIt.repository;

import com.p5Project.cookIt.entities.Comment;
import com.p5Project.cookIt.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findByRecipe(Recipe recipe);

    List<Comment> findByUser_Id(String userId);}