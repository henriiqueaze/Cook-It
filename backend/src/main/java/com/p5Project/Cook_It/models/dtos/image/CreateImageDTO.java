package com.p5Project.Cook_It.models.dtos.image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateImageDTO {
    @NotNull
    private UUID recipeId;

    @NotBlank
    private String url;

    private Integer width;
    private Integer height;
    private String contentType;
}
