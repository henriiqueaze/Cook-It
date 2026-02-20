package com.p5Project.cookIt.models.dtos.ingredient;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateIngredientDTO {

    @NotBlank
    private String name;

    private String unitDefault;
}