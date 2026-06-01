package com.p5Project.cookIt.dtos;

public record BannedWordDTO(
        String id,
        String term,
        boolean appliesToRecipes,
        boolean appliesToIngredients,
        boolean appliesToComments,
        String createdAt
) {
}