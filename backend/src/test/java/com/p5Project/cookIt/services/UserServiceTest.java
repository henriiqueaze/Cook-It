package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldUpdateUser() {
        // simula um user ja existente
        User user = new User();
        user.setId("1");
        user.setName("nome antigo");
        user.setEmail("emailantigo@email.com");

        // dados que vao substituir os antigos
        UpdateUserRequest request = new UpdateUserRequest("nome novo", "emailnovo@email.com");

        // simula busca do usuário no banco
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        doAnswer(invocation -> {
            UpdateUserRequest req = invocation.getArgument(0);
            User target = invocation.getArgument(1);
            target.setName(req.name());
            target.setEmail(req.email());
            return null;
        }).when(userMapper).updateUserFromRequest(any(UpdateUserRequest.class), any(User.class));

        // simula o save retornando o próprio usuário atualizado
        when(userRepository.save(any(User.class))).thenReturn(user);

        // simula o mapper que retorna o dto de user
        when(userMapper.toDTO(any(User.class))).thenReturn(null);

        // executa a atualização
        userService.updateUser("1", request, null);

        // captura o usuário que foi salvo
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        // verifica se os dois campos foram alterados
        assertEquals("nome novo", savedUser.getName());
        assertEquals("emailnovo@email.com", savedUser.getEmail());
    }
}