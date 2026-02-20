package com.p5Project.Cook_It.mappers;

import com.p5Project.Cook_It.models.dtos.user.CreateUserDTO;
import com.p5Project.Cook_It.models.dtos.user.UpdateUserDTO;
import com.p5Project.Cook_It.models.dtos.user.UserDTO;
import com.p5Project.Cook_It.models.entities.User;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    @Mapping(target = "passwordHash", source = "password") // lembrar do hash no service :)
    User toEntity(CreateUserDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateUserDTO dto, @MappingTarget User entity);

    default User fromId(UUID id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }
}
