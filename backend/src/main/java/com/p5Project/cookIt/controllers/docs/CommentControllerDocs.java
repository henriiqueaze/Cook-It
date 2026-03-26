package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.requests.CreateCommentRequest;
import com.p5Project.cookIt.dtos.requests.UpdateCommentRequest;
import com.p5Project.cookIt.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

public interface CommentControllerDocs {
    @Operation(summary = "Criar comentário", description = "Adiciona um comentário em uma receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentário criado",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class)))
    })
    CommentDTO createComment(@Valid @RequestBody CreateCommentRequest request,
                             @AuthenticationPrincipal UserPrincipal user);

    @Operation(summary = "Atualizar comentário", description = "Atualiza um comentário existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentário atualizado",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    })
    CommentDTO update(@PathVariable String id, @RequestBody UpdateCommentRequest request);

    @Operation(summary = "Excluir comentário", description = "Remove um comentário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comentário removido"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    })
    void delete(@PathVariable String id);
}
