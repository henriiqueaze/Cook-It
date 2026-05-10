package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record DeleteUserRequest(
        @NotBlank(message = "A senha é obrigatória") String password
) {
}

