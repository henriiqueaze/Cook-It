package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.dtos.requests.CreateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.UpdateRecipeRequest;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.RecipeIngredientMapper;
import com.p5Project.cookIt.mappers.RecipeMapper;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @Mock
    private RecipeIngredientMapper recipeIngredientMapper;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void shouldCreateRecipe() {
        // usuário dono da receita
        User user = new User();
        user.setId("1");
        user.setName("teste");

        // dados da receita
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Bolo",
                "Bolo simples",
                30,
                4,
                List.of(new RecipeIngredientDTO("sal", 1.0, "g")),
                List.of("Misturar", "Assar")
        );

        // simula o usuário encontrado
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // simula o mapper da receita
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(new RecipeDTO());

        // executa a criação
        recipeService.createRecipe(request, "1", null);

        // captura a receita salva
        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).saveAndFlush(recipeCaptor.capture());

        Recipe savedRecipe = recipeCaptor.getValue();

        // confere os dados principais
        assertEquals("Bolo", savedRecipe.getName());
        assertEquals("Bolo simples", savedRecipe.getDescription());
        assertEquals(30, savedRecipe.getPrepTime());
        assertEquals(4, savedRecipe.getPortions());
        assertEquals(user, savedRecipe.getAuthor());
    }

    @Test
    void shouldUpdateRecipe() {
        // usuário dono da receita
        User user = new User();
        user.setId("1");

        // receita já existente
        Recipe recipe = new Recipe();
        recipe.setId("10");
        recipe.setName("Bolo antigo");
        recipe.setDescription("Descrição antiga");
        recipe.setAuthor(user);

        // novos dados
        UpdateRecipeRequest request = new UpdateRecipeRequest(null, "Descrição nova", null, null, null, null);

        // simula busca da receita
        when(recipeRepository.findById("10")).thenReturn(Optional.of(recipe));

        // simula o update do mapper
        doAnswer(invocation -> {
            UpdateRecipeRequest req = invocation.getArgument(0);
            Recipe rec = invocation.getArgument(1);
            rec.setDescription(req.description());
            return null;
        }).when(recipeMapper).updateRecipeFromRequest(any(UpdateRecipeRequest.class), any(Recipe.class));

        // simula o mapper de saída
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(new RecipeDTO());

        // executa a edição
        recipeService.updateRecipe("10", request, null, "1");

        // captura a receita salva
        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).saveAndFlush(recipeCaptor.capture());

        Recipe savedRecipe = recipeCaptor.getValue();

        // confere se a descrição mudou
        assertEquals("Descrição nova", savedRecipe.getDescription());
    }

    @Test
    void shouldDeleteRecipe() {
        // executa a deleção
        recipeService.deleteRecipe("10");

        // verifica se apagou das tabelas auxiliares e da tabela principal
        verify(recipeRepository).deleteFromFavoritesByRecipeId("10");
        verify(recipeRepository).deleteFromUserRatingsByRecipeId("10");
        verify(recipeRepository).deleteById("10");
    }
}