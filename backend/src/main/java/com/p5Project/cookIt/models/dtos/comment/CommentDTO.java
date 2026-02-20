package com.p5Project.cookIt.models.dtos.comment;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private UUID id;
    private UUID recipeId;
    private UUID userId;
    private String content;
    private Instant createdAt;
}
