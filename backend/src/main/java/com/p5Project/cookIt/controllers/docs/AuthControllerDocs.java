package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.ChangePasswordRequest;
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

    @Operation(summary = "Registrar usuario", description = "Cria uma nova conta de usuario e envia um email de confirmacao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado com sucesso",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos")
    })
    AuthResponse register(@Valid @RequestBody RegisterRequest request);

    @Operation(summary = "Login", description = "Autentica o usuario e retorna um token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais invalidas"),
            @ApiResponse(responseCode = "403", description = "Email nao confirmado ou conta sem permissao")
    })
    AuthResponse login(@Valid @RequestBody LoginRequest request);

    @Operation(summary = "Logout", description = "Encerra a sessao do usuario (JWT e stateless)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout realizado")
    })
    void logout();

    @Operation(summary = "Usuario logado", description = "Retorna os dados do usuario autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    })
    UserDTO me(@AuthenticationPrincipal UserPrincipal userPrincipal);

    @Operation(summary = "Confirmar email", description = "Confirma o email do usuario usando o token enviado por email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email confirmado"),
            @ApiResponse(responseCode = "400", description = "Token invalido ou expirado")
    })
    String confirmEmail(@RequestParam String token);

    @Operation(summary = "Reenviar confirmacao de email", description = "Reenvia o email de confirmacao respeitando um intervalo minimo de 5 minutos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Confirmacao reenviada"),
            @ApiResponse(responseCode = "400", description = "Email ja confirmado ou reenvio solicitado recentemente"),
            @ApiResponse(responseCode = "404", description = "Email nao encontrado")
    })
    void resendConfirmationEmail(@RequestBody ForgotPasswordRequest request);

    @Operation(summary = "Solicitar redefinicao de senha", description = "Envia um codigo por email para redefinir a senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitacao enviada"),
            @ApiResponse(responseCode = "400", description = "Reenvio solicitado recentemente"),
            @ApiResponse(responseCode = "404", description = "Email nao encontrado")
    })
    void forgotPassword(@RequestBody ForgotPasswordRequest request);

    @Operation(summary = "Redefinir senha", description = "Redefine a senha usando o codigo enviado por email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha redefinida"),
            @ApiResponse(responseCode = "400", description = "Token invalido")
    })
    void resetPassword(@RequestBody ResetPasswordRequest request);

    @Operation(summary = "Alterar senha", description = "Altera a senha atual do usuario logado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha alterada"),
            @ApiResponse(responseCode = "400", description = "Senha atual invalida"),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    })
    void changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ChangePasswordRequest request
    );

    @Operation(summary = "Validar codigo de redefinicao", description = "Valida o codigo enviado por email antes de permitir a nova senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Codigo valido"),
            @ApiResponse(responseCode = "400", description = "Codigo invalido ou expirado")
    })
    void validateResetCode(@RequestBody ResetPasswordRequest request);
}
