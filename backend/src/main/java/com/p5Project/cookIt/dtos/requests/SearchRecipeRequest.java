package com.p5Project.cookIt.dtos.requests;

import lombok.Data;
import java.util.List;

@Data
public class SearchRecipeRequest {

    private List<String> ingredients;

    private Boolean exactMatch = false;

    private String sortBy;
}