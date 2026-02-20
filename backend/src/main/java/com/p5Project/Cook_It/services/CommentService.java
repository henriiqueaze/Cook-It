package com.p5Project.Cook_It.services;

import com.p5Project.Cook_It.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentService {

    @Autowired
    private CommentRepository repository;
}
