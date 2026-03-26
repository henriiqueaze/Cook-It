package com.p5Project.cookIt.dtos;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class UserDTO {

    private String id;
    private String name;
    private String email;
    private String photo;

    private List<String> createdRecipes;
    private List<String> favoriteRecipes;

    private Map<String, Integer> ratings;
}