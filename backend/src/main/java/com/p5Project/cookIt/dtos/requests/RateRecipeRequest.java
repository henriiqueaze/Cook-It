package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RateRecipeRequest {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}