package com.p5Project.cookIt.dtos;

import java.util.List;
import java.util.Map;

public record UserDTO(
        String id,
        String name,
        String email,
        String photo,
        String role,
        boolean banned,
        boolean emailVerified,
        List<String> createdRecipes,
        List<String> favoriteRecipes,
        Map<String, Integer> ratings
) {
}