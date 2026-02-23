package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.CommentDTO;
import com.p5Project.cookIt.models.entities.Comment;
import com.p5Project.cookIt.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository repository;

    public CommentDTO findCommentById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        var dto = Mapper.parseItem(entity, CommentDTO.class);

        return dto;
    }

    public List<CommentDTO> findAllComments() {
        var entities = repository.findAll();
        var dto = Mapper.parseItemsList(entities, CommentDTO.class);

        return dto;
    }

    public CommentDTO createComment(CommentDTO comment) {
        var entity = Mapper.parseItem(comment, Comment.class);
        repository.save(entity);

        var dto = Mapper.parseItem(entity, CommentDTO.class);
        return dto;
    }

    public CommentDTO updateComment(CommentDTO comment) {
        var entity = repository.findById(comment.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(comment, entity);

        repository.save(entity);

        var dto = Mapper.parseItem(entity, CommentDTO.class);
        return dto;
    }

    public CommentDTO updateCommentField(UUID id, CommentDTO comment) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        Mapper.mapNonNullFields(comment, entity);

        repository.save(entity);

        var dto = Mapper.parseItem(entity, CommentDTO.class);
        return dto;
    }

    public void deleteComment(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        var dto = Mapper.parseItem(entity, CommentDTO.class);

        repository.delete(entity);
    }
}
