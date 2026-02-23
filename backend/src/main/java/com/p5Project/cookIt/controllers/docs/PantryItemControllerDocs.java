package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.models.dtos.PantryItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface PantryItemControllerDocs {
    @Operation(summary = "Buscar item da despensa por ID", description = "Retorna um item da despensa específico com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado", content = @Content(schema = @Schema(implementation = PantryItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    PantryItemDTO findPantryItemById(@Parameter(description = "ID do item", required = true) @PathVariable UUID id);

    @Operation(summary = "Listar todos os itens da despensa", description = "Retorna uma lista com todos os itens da despensa cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = PantryItemDTO.class)))
    })
    List<PantryItemDTO> findAllPantryItems();

    @Operation(summary = "Criar novo item da despensa", description = "Cria um novo item na despensa do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item criado com sucesso", content = @Content(schema = @Schema(implementation = PantryItemDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    PantryItemDTO createPantryItem(@RequestBody(description = "Objeto contendo os dados do item da despensa", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment);

    @Operation(summary = "Atualizar item completo da despensa", description = "Atualiza todas as informações de um item existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso", content = @Content(schema = @Schema(implementation = PantryItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    PantryItemDTO updatePantryItem(@RequestBody(description = "Objeto contendo os dados atualizados do item", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment);

    @Operation(summary = "Atualizar campo específico da despensa", description = "Atualiza apenas campos específicos de um item existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado parcialmente com sucesso", content = @Content(schema = @Schema(implementation = PantryItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    PantryItemDTO updatePantryItemField(@Parameter(description = "ID do item", required = true) @PathVariable UUID id,
                                        @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = PantryItemDTO.class))) @org.springframework.web.bind.annotation.RequestBody PantryItemDTO comment);

    @Operation(summary = "Deletar item da despensa", description = "Remove um item da despensa com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    ResponseEntity<?> deletePantryItem(@Parameter(description = "ID do item", required = true) @PathVariable UUID id);
}
