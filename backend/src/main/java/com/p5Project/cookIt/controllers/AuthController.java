package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.AuthControllerDocs;
import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.ChangePasswordRequest;
import com.p5Project.cookIt.dtos.requests.ForgotPasswordRequest;
import com.p5Project.cookIt.dtos.requests.LoginRequest;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.dtos.requests.ResetPasswordRequest;
import com.p5Project.cookIt.dtos.responses.AuthResponse;
import com.p5Project.cookIt.security.UserPrincipal;
import com.p5Project.cookIt.services.AuthService;
import com.p5Project.cookIt.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação", description = "Gerenciamento de autenticação")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    @Override
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Override
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    @Override
    public void logout() {
    }

    @GetMapping("/me")
    @Override
    public UserDTO me(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getUserById(userPrincipal.getId());
    }

    @GetMapping("/confirm-email")
    @Override
    public String confirmEmail(@RequestParam String token) {
        return authService.confirmEmail(token);
    }

    @PostMapping("/resend-confirmation-email")
    @Override
    public void resendConfirmationEmail(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.resendConfirmationEmail(request.email());
    }

    @PostMapping("/forgot-password")
    @Override
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.email());
    }

    @PostMapping("/reset-password")
    @Override
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.newPassword());
    }

    @PostMapping("/change-password")
    @Override
    public void changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(userPrincipal.getId(), request);
    }

    @PostMapping("/validate-reset-code")
    @Override
    public void validateResetCode(@RequestBody ResetPasswordRequest request) {
        authService.validateResetCode(request.token());
    }
}
