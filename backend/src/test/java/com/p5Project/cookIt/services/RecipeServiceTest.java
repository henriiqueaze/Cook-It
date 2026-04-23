package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.dtos.requests.CreateRecipeRequest;
import com.p5Project.cookIt.dtos.requests.UpdateRecipeRequest;
import com.p5Project.cookIt.entities.Recipe;
import com.p5Project.cookIt.entities.RecipeIngredient;
import com.p5Project.cookIt.entities.User;
import com.p5Project.cookIt.mappers.RecipeIngredientMapper;
import com.p5Project.cookIt.mappers.RecipeMapper;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Testes 03, 04 e 05 - Receitas
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

    // Teste 03 - Criação de Receita
    @Test
    void deveCriarReceitaComSucesso() {
        // Dados que serão enviados para criar a receita
        String userId = "user-123";
        CreateRecipeRequest request = new CreateRecipeRequest();
        request.setName("Macarrão ao Molho");
        request.setDescription("Receita deliciosa");
        request.setPrepTime(30);
        request.setPortions(4);
        request.setInstructions(List.of("Cozinhe o macarrão", "Prepare o molho"));

        RecipeIngredientDTO ingrediente = new RecipeIngredientDTO();
        ingrediente.setIngredient("macarrão");
        ingrediente.setQuantity(200.0);
        ingrediente.setUnit("g");
        request.setIngredients(List.of(ingrediente));

        // Simulando o autor da receita
        User autor = new User();
        autor.setId(userId);
        autor.setName("Chef João");

        // Simulando os dados retornados
        RecipeDTO receitaDTO = new RecipeDTO();
        receitaDTO.setId("recipe-456");
        receitaDTO.setName("Macarrão ao Molho");
        receitaDTO.setAuthorId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(autor));
        when(recipeIngredientMapper.toEntity(any(RecipeIngredientDTO.class))).thenReturn(new RecipeIngredient());
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(receitaDTO);

        // Executando a criação (null = sem imagem)
        RecipeDTO resultado = recipeService.createRecipe(request, userId, null);

        // Verificando se os dados estão corretos
        assertNotNull(resultado);
        assertEquals("Macarrão ao Molho", resultado.getName());
        assertEquals(userId, resultado.getAuthorId());

        // Verificando se a receita foi salva no banco invocando 1 vez
        verify(recipeRepository, times(1)).saveAndFlush(any(Recipe.class));
    }

    // Teste 04 - Edição de Receita
    @Test
    void deveEditarReceitaComSucesso() {
        // Dados que serão enviados para editar a receita
        String recipeId = "recipe-456";
        String userId = "user-123";
        UpdateRecipeRequest request = new UpdateRecipeRequest();
        request.setName("Macarrão Carbonara");
        request.setPrepTime(25);

        // Autor da receita (mesmo usuário que está editando)
        User autor = new User();
        autor.setId(userId);

        // Receita que ja existe no banco
        Recipe receitaExistente = new Recipe();
        receitaExistente.setId(recipeId);
        receitaExistente.setName("Macarrão ao Molho");
        receitaExistente.setAuthor(autor);

        // Objeto com os dados atualizados
        RecipeDTO receitaAtualizada = new RecipeDTO();
        receitaAtualizada.setId(recipeId);
        receitaAtualizada.setName("Macarrão Carbonara");

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(receitaExistente));
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(receitaAtualizada);

        // Executando a edição (null = sem imagem nova)
        RecipeDTO resultado = recipeService.updateRecipe(recipeId, request, null, userId);

        // Verificando se os dados foram atualizados
        assertNotNull(resultado);
        assertEquals("Macarrão Carbonara", resultado.getName());

        // Verificando o salvamento da receita ivocando 1 vez 
        verify(recipeRepository, times(1)).saveAndFlush(any(Recipe.class));
    }

    // Teste 05 - Exclusão de Receita
    @Test
    void deveExcluirReceitaComSucesso() {
        // ID da receita que será excluída
        String recipeId = "recipe-456";

        // Executando a exclusão
        recipeService.deleteRecipe(recipeId);

        // Verificando se todas as etapas de exclusão foram chamadas
        verify(recipeRepository, times(1)).deleteFromFavoritesByRecipeId(recipeId);
        verify(recipeRepository, times(1)).deleteFromUserRatingsByRecipeId(recipeId);
        verify(recipeRepository, times(1)).deleteById(recipeId);
    }
}
