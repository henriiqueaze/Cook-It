package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.UserRepository;
import com.p5Project.cookIt.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldCreateUser() {
        // cria um request simples
        RegisterRequest request = new RegisterRequest();
        request.setName("teste");
        request.setEmail("teste@email.com");
        request.setPassword("teste123");

        // define o comportamento dos mocks
        when(passwordEncoder.encode("teste123")).thenReturn("senha_criptografada");
        when(userMapper.toDTO(any(User.class))).thenReturn(new UserDTO());
        when(jwtService.generateToken(isNull())).thenReturn("token_fake");

        // executa o metodo
        authService.register(request);

        // captura o usuário salvo no banco
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        // verifica se os dados estão corretos
        assertEquals("teste", savedUser.getName());
        assertEquals("teste@email.com", savedUser.getEmail());

        // verifica se a senha foi criptografada
        assertEquals("senha_criptografada", savedUser.getPassword());
    }
}