package com.p5Project.cookIt.dtos;

public record AdminSummaryDTO(
        long totalUsers,
        long adminUsers,
        long bannedUsers,
        long totalRecipes,
        long totalIngredients,
        long totalComments,
        long bannedWords
) {
}