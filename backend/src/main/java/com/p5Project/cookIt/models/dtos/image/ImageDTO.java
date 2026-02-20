package com.p5Project.cookIt.models.dtos.image;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {

    private UUID id;
    private UUID recipeId;
    private String url;
    private Integer width;
    private Integer height;
    private String contentType;
    private Instant uploadedAt;
}