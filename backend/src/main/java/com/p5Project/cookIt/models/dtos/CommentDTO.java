package com.p5Project.cookIt.models.dtos;


import com.p5Project.cookIt.models.entities.Recipe;
import com.p5Project.cookIt.models.entities.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO extends RepresentationModel<CommentDTO> {

    private UUID id;
    private Recipe recipe;
    private User user;
    private String content;
    private LocalDateTime createdAt;

}

