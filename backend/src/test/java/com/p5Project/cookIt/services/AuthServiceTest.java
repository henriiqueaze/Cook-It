package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.dtos.responses.AuthResponse;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.PasswordResetTokenRepository;
import com.p5Project.cookIt.repository.UserRepository;
import com.p5Project.cookIt.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Teste 01 - Criação de Usuário
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveCriarUsuarioComSucesso() {
        // Dados que serão enviados para criar o usuário
        RegisterRequest request = new RegisterRequest();
        request.setName("João Silva");
        request.setEmail("joao@email.com");
        request.setPassword("senha123");

        // Simulando o que o banco retorna após salvar
        User usuarioSalvo = new User();
        usuarioSalvo.setId("id-123");
        usuarioSalvo.setName("João Silva");
        usuarioSalvo.setEmail("joao@email.com");

        // Simulando o DTO(os dados) que o mapper retorna
        UserDTO usuarioDTO = new UserDTO();
        usuarioDTO.setId("id-123");
        usuarioDTO.setName("João Silva");
        usuarioDTO.setEmail("joao@email.com");

        when(passwordEncoder.encode("senha123")).thenReturn("senha_criptografada");
        when(userRepository.save(any(User.class))).thenReturn(usuarioSalvo);
        when(userMapper.toDTO(any(User.class))).thenReturn(usuarioDTO);
        when(jwtService.generateToken(any())).thenReturn("token-jwt");

        // Executando o registro
        AuthResponse resposta = authService.register(request);

        // Verificando se os dados estão corretos
        assertNotNull(resposta);
        assertNotNull(resposta.getUser());
        assertEquals("João Silva", resposta.getUser().getName());
        assertEquals("joao@email.com", resposta.getUser().getEmail());
        assertEquals("token-jwt", resposta.getToken());

        // Verificando se o usuário foi salvo no banco
        verify(userRepository, times(1)).save(any(User.class));
    }
}
