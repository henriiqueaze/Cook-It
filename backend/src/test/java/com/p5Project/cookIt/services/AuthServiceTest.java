package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.UserMapper;

import com.p5Project.cookIt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    // email-related collaborators removed from test

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldCreateUser() {
        RegisterRequest request = new RegisterRequest("teste", "teste@email.com", "teste123");

        when(passwordEncoder.encode("teste123")).thenReturn("senha_criptografada");
        when(userMapper.toDTO(any(User.class))).thenReturn(null);

        authService.register(request);

        verify(userRepository).save(any(User.class));
    }
}