package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.UserDTO;
import com.p5Project.cookIt.models.entities.User;
import com.p5Project.cookIt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserDTO findUserById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, UserDTO.class);
    }

    public List<UserDTO> findAllUsers() {
        var entities = repository.findAll();
        return Mapper.parseItemsList(entities, UserDTO.class);
    }

    public UserDTO createUser(UserDTO user) {
        var entity = Mapper.parseItem(user, User.class);
        repository.save(entity);
        return Mapper.parseItem(entity, UserDTO.class);
    }

    public UserDTO updateUser(UserDTO user) {
        var entity = repository.findById(user.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(user, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, UserDTO.class);
    }

    public UserDTO updateUserField(UUID id, UserDTO user) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(user, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, UserDTO.class);
    }

    public void deleteUser(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }
}
