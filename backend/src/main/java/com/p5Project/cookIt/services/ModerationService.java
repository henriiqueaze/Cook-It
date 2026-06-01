package com.p5Project.cookIt.services;

import com.p5Project.cookIt.dtos.RecipeIngredientDTO;
import com.p5Project.cookIt.dtos.requests.CreateBannedWordRequest;
import com.p5Project.cookIt.entities.BannedWord;
import com.p5Project.cookIt.exceptions.ForbiddenOperationException;
import com.p5Project.cookIt.repository.BannedWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModerationService {

    private final BannedWordRepository bannedWordRepository;

    @Transactional(readOnly = true)
    public List<BannedWord> getAllBannedWords() {
        return bannedWordRepository.findAll();
    }

    public BannedWord createBannedWord(CreateBannedWordRequest request) {
        String normalizedTerm = normalizeTerm(request.term());
        ensureScopesSelected(request.appliesToRecipes(), request.appliesToIngredients(), request.appliesToComments());

        bannedWordRepository.findByTermIgnoreCase(normalizedTerm).ifPresent(existing -> {
            throw new ForbiddenOperationException("Essa palavra já está banida");
        });

        BannedWord bannedWord = new BannedWord();
        bannedWord.setTerm(normalizedTerm);
        bannedWord.setAppliesToRecipes(Boolean.TRUE.equals(request.appliesToRecipes()));
        bannedWord.setAppliesToIngredients(Boolean.TRUE.equals(request.appliesToIngredients()));
        bannedWord.setAppliesToComments(Boolean.TRUE.equals(request.appliesToComments()));
        return bannedWordRepository.save(bannedWord);
    }

    public BannedWord updateBannedWord(String id, CreateBannedWordRequest request) {
        BannedWord bannedWord = bannedWordRepository.findById(id)
                .orElseThrow(() -> new ForbiddenOperationException("Palavra banida não encontrada"));

        String normalizedTerm = normalizeTerm(request.term());
        ensureScopesSelected(request.appliesToRecipes(), request.appliesToIngredients(), request.appliesToComments());

        bannedWordRepository.findByTermIgnoreCase(normalizedTerm)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ForbiddenOperationException("Essa palavra já está banida");
                });

        bannedWord.setTerm(normalizedTerm);
        bannedWord.setAppliesToRecipes(Boolean.TRUE.equals(request.appliesToRecipes()));
        bannedWord.setAppliesToIngredients(Boolean.TRUE.equals(request.appliesToIngredients()));
        bannedWord.setAppliesToComments(Boolean.TRUE.equals(request.appliesToComments()));
        return bannedWordRepository.save(bannedWord);
    }

    public void deleteBannedWord(String id) {
        bannedWordRepository.deleteById(id);
    }

    public void ensureRecipeAllowed(String name, String description, List<RecipeIngredientDTO> ingredients, List<String> instructions) {
        assertContentAllowed(name, Scope.RECIPE, "Sua receita contém uma palavra bloqueada. Remova o termo banido e tente novamente.");
        assertContentAllowed(description, Scope.RECIPE, "Sua receita contém uma palavra bloqueada. Remova o termo banido e tente novamente.");

        if (ingredients != null) {
            for (RecipeIngredientDTO ingredient : ingredients) {
                if (ingredient != null) {
                    assertContentAllowed(ingredient.ingredient(), Scope.RECIPE, "Sua receita contém um ingrediente com palavra bloqueada. Remova o termo banido e tente novamente.");
                }
            }
        }

        if (instructions != null) {
            for (String instruction : instructions) {
                assertContentAllowed(instruction, Scope.RECIPE, "Sua receita contém uma instrução com palavra bloqueada. Remova o termo banido e tente novamente.");
            }
        }
    }

    public void ensureIngredientAllowed(String name) {
        assertContentAllowed(name, Scope.INGREDIENT, "O nome do ingrediente contém uma palavra bloqueada. Remova o termo banido e tente novamente.");
    }

    public void ensureCommentAllowed(String text) {
        assertContentAllowed(text, Scope.COMMENT, "Seu comentário contém uma palavra bloqueada. Remova o termo banido e tente novamente.");
    }

    private void assertContentAllowed(String content, Scope scope, String message) {
        if (content == null || content.isBlank()) {
            return;
        }

        String normalizedContent = content.toLowerCase();

        boolean blocked = bannedWordRepository.findAll().stream()
                .filter(bannedWord -> appliesToScope(bannedWord, scope))
                .anyMatch(bannedWord -> normalizedContent.contains(bannedWord.getTerm().toLowerCase()));

        if (blocked) {
            throw new ForbiddenOperationException(message);
        }
    }

    private boolean appliesToScope(BannedWord bannedWord, Scope scope) {
        return switch (scope) {
            case RECIPE -> bannedWord.isAppliesToRecipes();
            case INGREDIENT -> bannedWord.isAppliesToIngredients();
            case COMMENT -> bannedWord.isAppliesToComments();
        };
    }

    private String normalizeTerm(String term) {
        String normalized = term == null ? null : term.trim();

        if (normalized == null || normalized.isBlank()) {
            throw new ForbiddenOperationException("O termo da palavra banida é obrigatório");
        }

        return normalized.toLowerCase();
    }

    private void ensureScopesSelected(Boolean recipes, Boolean ingredients, Boolean comments) {
        if (!Boolean.TRUE.equals(recipes) && !Boolean.TRUE.equals(ingredients) && !Boolean.TRUE.equals(comments)) {
            throw new ForbiddenOperationException("Selecione ao menos um contexto para a palavra banida");
        }
    }

    private enum Scope {
        RECIPE,
        INGREDIENT,
        COMMENT
    }
}