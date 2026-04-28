package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.requests.DeleteUserRequest;
import com.p5Project.cookIt.dtos.requests.UpdateUserRequest;
import com.p5Project.cookIt.entities.Ingredient;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.RecipeIngredient;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.exceptions.ForbiddenOperationException;
import com.p5Project.cookIt.exceptions.InvalidCredentialsException;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.CommentRepository;
import com.p5Project.cookIt.repository.EmailVerificationTokenRepository;
import com.p5Project.cookIt.repository.IngredientRepository;
import com.p5Project.cookIt.repository.PasswordResetTokenRepository;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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

    @Test
    void shouldDeleteUserAccount() {
        User user = new User();
        user.setId("1");

        Recipe remainingRecipe = new Recipe();
        RecipeIngredient remainingIngredient = new RecipeIngredient();
        remainingIngredient.setIngredient("tomate");
        remainingRecipe.setIngredients(List.of(remainingIngredient));

        Ingredient usedIngredient = new Ingredient();
        usedIngredient.setName("tomate");

        Ingredient orphanIngredient = new Ingredient();
        orphanIngredient.setName("cebola");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", user.getPassword())).thenReturn(true);
        when(recipeRepository.findAll()).thenReturn(List.of(remainingRecipe));
        when(ingredientRepository.findAll()).thenReturn(List.of(usedIngredient, orphanIngredient));

        DeleteUserRequest request = new DeleteUserRequest("senha123");

        userService.deleteUser("1", "1", request);

        verify(commentRepository).deleteByUserId("1");
        verify(passwordResetTokenRepository).deleteAllByUser(user);
        verify(emailVerificationTokenRepository).deleteAllByUser(user);
        verify(recipeRepository).deleteByAuthorId("1");
        verify(ingredientRepository).deleteAllInBatch(argThat(this::hasOnlyOrphanIngredient));
        verify(userRepository).delete(user);
    }

    @Test
    void shouldNotDeleteAnotherUserAccount() {
        DeleteUserRequest request = new DeleteUserRequest("senha123");

        assertThrows(ForbiddenOperationException.class, () -> userService.deleteUser("1", "2", request));

        verifyNoInteractions(userRepository, recipeRepository, passwordResetTokenRepository, emailVerificationTokenRepository, commentRepository, ingredientRepository, passwordEncoder);
    }

    @Test
    void shouldRejectInvalidPasswordWhenDeletingUser() {
        User user = new User();
        user.setId("1");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("errada", user.getPassword())).thenReturn(false);

        DeleteUserRequest request = new DeleteUserRequest("errada");

        assertThrows(InvalidCredentialsException.class, () -> userService.deleteUser("1", "1", request));

        verify(passwordEncoder).matches("errada", user.getPassword());
        verifyNoInteractions(commentRepository, recipeRepository, passwordResetTokenRepository, emailVerificationTokenRepository, ingredientRepository);
    }

    @Test
    void shouldValidatePasswordForDeleteWithoutDeletingUser() {
        User user = new User();
        user.setId("1");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", user.getPassword())).thenReturn(true);

        DeleteUserRequest request = new DeleteUserRequest("senha123");

        userService.validateDeletePassword("1", "1", request);

        verify(passwordEncoder).matches("senha123", user.getPassword());
        verifyNoInteractions(commentRepository, recipeRepository, passwordResetTokenRepository, emailVerificationTokenRepository, ingredientRepository);
        verify(userRepository).findById("1");
    }

    @Test
    void shouldRejectInvalidPasswordWhenValidatingForDelete() {
        User user = new User();
        user.setId("1");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("errada", user.getPassword())).thenReturn(false);

        DeleteUserRequest request = new DeleteUserRequest("errada");

        assertThrows(InvalidCredentialsException.class, () -> userService.validateDeletePassword("1", "1", request));

        verify(passwordEncoder).matches("errada", user.getPassword());
        verifyNoInteractions(commentRepository, recipeRepository, passwordResetTokenRepository, emailVerificationTokenRepository, ingredientRepository);
        verify(userRepository).findById("1");
    }

    private boolean hasOnlyOrphanIngredient(Iterable<Ingredient> ingredients) {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredients.forEach(ingredientList::add);
        return ingredientList.size() == 1 && "cebola".equals(ingredientList.get(0).getName());
    }
}