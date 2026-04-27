package com.p5Project.cookIt.dtos.requests;

import java.util.List;

public record SearchRecipeRequest(
        List<String> ingredients,
        boolean exactMatch,
        String sortBy
) {
}