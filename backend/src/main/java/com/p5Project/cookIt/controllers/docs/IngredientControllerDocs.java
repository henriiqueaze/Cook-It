package com.p5Project.cookIt.controllers.docs;

import com.p5Project.cookIt.dtos.IngredientDTO;
import com.p5Project.cookIt.dtos.requests.CreateIngredientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IngredientControllerDocs {
    @Operation(summary = "Listar ingredientes", description = "Retorna todos os ingredientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada",
                    content = @Content(schema = @Schema(implementation = IngredientDTO.class)))
    })
    List<IngredientDTO> getAll();

    @Operation(summary = "Buscar ingrediente", description = "Busca ingredientes pelo nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultados encontrados",
                    content = @Content(schema = @Schema(implementation = IngredientDTO.class)))
    })
    List<IngredientDTO> search(@RequestParam String q);

    @Operation(summary = "Criar ingrediente", description = "Adiciona um novo ingrediente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingrediente criado",
                    content = @Content(schema = @Schema(implementation = IngredientDTO.class)))
    })
    IngredientDTO create(@Valid @RequestBody CreateIngredientRequest request);
}
