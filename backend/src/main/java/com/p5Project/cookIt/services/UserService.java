package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.models.dtos.user.CreateUserDTO;
import com.p5Project.cookIt.models.dtos.user.UserDTO;
import com.p5Project.cookIt.models.dtos.user.UpdateUserDTO;
import com.p5Project.cookIt.models.entities.User;
import com.p5Project.cookIt.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repo;
    private final ModelMapper mapper;

    public UserService(UserRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public UserDTO create(CreateUserDTO dto) {
        User u = mapper.map(dto, User.class);
        User saved = repo.save(u);
        return mapper.map(saved, UserDTO.class);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(UUID id) {
        User u = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapper.map(u, UserDTO.class);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return repo.findAll().stream().map(x -> mapper.map(x, UserDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public UserDTO update(UUID id, UpdateUserDTO dto) {
        User ent = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        mapper.map(dto, ent);
        User saved = repo.save(ent);
        return mapper.map(saved, UserDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("User not found");
        repo.deleteById(id);
    }
}
