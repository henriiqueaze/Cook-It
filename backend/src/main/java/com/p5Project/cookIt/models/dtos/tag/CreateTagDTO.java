package com.p5Project.cookIt.models.dtos.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTagDTO {
    @NotBlank
    private String name;
}
