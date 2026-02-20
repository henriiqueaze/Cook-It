package com.p5Project.cookIt.models.dtos.tag;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDTO {
    private UUID id;
    private String name;
}
