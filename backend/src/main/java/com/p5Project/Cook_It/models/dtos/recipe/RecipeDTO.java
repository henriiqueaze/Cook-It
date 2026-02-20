package com.p5Project.Cook_It.models.dtos.recipe;

import com.p5Project.Cook_It.models.dtos.image.ImageDTO;
import com.p5Project.Cook_It.models.dtos.tag.TagDTO;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {
    private UUID id;
    private UUID authorId;
    private String title;
    private String description;
    private String steps;
    private Integer servings;
    private Integer prepTimeMinutes;
    private Boolean published;
    private Instant createdAt;
    private Instant updatedAt;

    private List<ImageDTO> images;
    private List<TagDTO> tags;
}