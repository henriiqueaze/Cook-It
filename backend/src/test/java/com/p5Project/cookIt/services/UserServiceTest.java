package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Teste 02 - Edição de Usuário
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private UserService userService;

    @Test
    void deveEditarUsuarioComSucesso() {
        // Dados que serão enviados para editar o usuário
        String userId = "user-123";
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Novo Nome");
        request.setEmail("novo@email.com");

        // Usuário que já existe no banco
        User usuarioExistente = new User();
        usuarioExistente.setId(userId);
        usuarioExistente.setName("Nome Antigo");
        usuarioExistente.setEmail("antigo@email.com");

        // DTO com os dados atualizados
        UserDTO usuarioAtualizado = new UserDTO();
        usuarioAtualizado.setId(userId);
        usuarioAtualizado.setName("Novo Nome");
        usuarioAtualizado.setEmail("novo@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(usuarioExistente));
        when(userRepository.save(any(User.class))).thenReturn(usuarioExistente);
        when(userMapper.toDTO(any(User.class))).thenReturn(usuarioAtualizado);

        // Executando a edição (null = sem foto nova)
        UserDTO resultado = userService.updateUser(userId, request, null);

        // Verificando se os dados foram atualizados
        assertNotNull(resultado);
        assertEquals("Novo Nome", resultado.getName());
        assertEquals("novo@email.com", resultado.getEmail());

        // Verificando se o usuário foi salvo no banco exatamente 1 vez
        verify(userRepository, times(1)).save(any(User.class));
    }
}
