package com.p5Project.cookIt.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Token is required") String token,
        @Size(min = 6, message = "Password must have at least 6 characters") String newPassword
) {
}