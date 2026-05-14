package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.ChangePasswordRequest;
import com.p5Project.cookIt.dtos.requests.LoginRequest;
import com.p5Project.cookIt.dtos.requests.RegisterRequest;
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

public interface AuthControllerDocs {

        @Operation(summary = "Registrar usuário", description = "Cria uma nova conta de usuário")
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
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
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

    // Email-based confirmation and reset endpoints removed

    @Operation(summary = "Alterar senha", description = "Altera a senha atual do usuário logado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha alterada"),
            @ApiResponse(responseCode = "400", description = "Senha atual inválida"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    void changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ChangePasswordRequest request
    );
}