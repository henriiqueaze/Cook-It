package com.p5Project.cookIt.models.dtos;

import com.p5Project.cookIt.models.entities.Recipe;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO extends RepresentationModel<ImageDTO> {

    private UUID id;
    private Recipe recipe;
    private String url;
    private Integer width;
    private Integer height;
    private String contentType;
    private Instant uploadedAt;
}
