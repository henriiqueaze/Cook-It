package com.p5Project.Cook_It.models.dtos.tag;

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
