package com.p5Project.cookIt.models.dtos.user;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
    private String displayName;
    private String avatarUrl;
}
