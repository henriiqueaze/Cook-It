package com.p5Project.cookIt.models.dtos;


import com.p5Project.cookIt.models.entities.Recipe;
import com.p5Project.cookIt.models.entities.User;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private UUID id;
    private Recipe recipe;
    private User user;
    private String content;
    private Instant createdAt;
}