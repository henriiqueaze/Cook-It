package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.ForgotPasswordRequest;
import com.p5Project.cookIt.dtos.requests.LoginRequest;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
import com.p5Project.cookIt.dtos.requests.ResetPasswordRequest;
import com.p5Project.cookIt.dtos.responses.AuthResponse;
import com.p5Project.cookIt.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AuthControllerDocs {

    @Operation(summary = "Registrar usuário", description = "Cria uma nova conta de usuário e envia um email de confirmação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    AuthResponse register(@Valid @RequestBody RegisterRequest request);

    @Operation(summary = "Login", description = "Autentica o usuário e retorna um token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "403", description = "Email não confirmado")
    })
    AuthResponse login(@Valid @RequestBody LoginRequest request);

    @Operation(summary = "Logout", description = "Encerra a sessão do usuário (JWT é stateless)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout realizado")
    })
    void logout();

    @Operation(summary = "Usuário logado", description = "Retorna os dados do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    UserDTO me(@AuthenticationPrincipal UserPrincipal userPrincipal);

    @Operation(summary = "Confirmar email", description = "Confirma o email do usuário usando o token enviado por email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email confirmado"),
            @ApiResponse(responseCode = "400", description = "Token inválido ou expirado")
    })
    String confirmEmail(@RequestParam String token);

    @Operation(summary = "Solicitar redefinição de senha", description = "Envia um token para redefinir a senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitação enviada"),
            @ApiResponse(responseCode = "404", description = "Email não encontrado")
    })
    void forgotPassword(@RequestBody ForgotPasswordRequest request);

    @Operation(summary = "Redefinir senha", description = "Redefine a senha usando o token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha redefinida"),
            @ApiResponse(responseCode = "400", description = "Token inválido")
    })
    void resetPassword(@RequestBody ResetPasswordRequest request);
}