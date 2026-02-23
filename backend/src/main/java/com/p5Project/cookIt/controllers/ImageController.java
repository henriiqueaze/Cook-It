package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.models.dtos.ImageDTO;
import com.p5Project.cookIt.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/image")
@Tag(name = "Image", description = "Endpoints para gerenciamento de imagens das receitas")
public class ImageController {

    @Autowired
    private ImageService service;

    @Operation(summary = "Buscar imagem por ID", description = "Retorna uma imagem específica com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem encontrada", content = @Content(schema = @Schema(implementation = ImageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ImageDTO findImageById(@Parameter(description = "ID da imagem", required = true) @PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Listar todas as imagens", description = "Retorna uma lista com todas as imagens cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = ImageDTO.class)))
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<ImageDTO> findAllImages() {
        return service.findAll();
    }

    @Operation(summary = "Criar nova imagem", description = "Cria uma nova imagem no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem criada com sucesso", content = @Content(schema = @Schema(implementation = ImageDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ImageDTO createImage(@RequestBody(description = "Objeto contendo os dados da imagem", required = true, content = @Content(schema = @Schema(implementation = ImageDTO.class))) @org.springframework.web.bind.annotation.RequestBody ImageDTO image) {
        return service.createImage(image);
    }

    @Operation(summary = "Atualizar imagem completa", description = "Atualiza todas as informações de uma imagem existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem atualizada com sucesso", content = @Content(schema = @Schema(implementation = ImageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada", content = @Content)
    })
    @PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ImageDTO updateImage(@RequestBody(description = "Objeto contendo os dados atualizados da imagem", required = true, content = @Content(schema = @Schema(implementation = ImageDTO.class))) @org.springframework.web.bind.annotation.RequestBody ImageDTO image) {
        return service.updateImage(image);
    }

    @Operation(summary = "Atualizar campo específico da imagem", description = "Atualiza apenas campos específicos de uma imagem existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem atualizada parcialmente com sucesso", content = @Content(schema = @Schema(implementation = ImageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada", content = @Content)
    })
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ImageDTO updateImageField(@Parameter(description = "ID da imagem", required = true) @PathVariable UUID id,
                                     @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = ImageDTO.class))) @org.springframework.web.bind.annotation.RequestBody ImageDTO image) {
        return service.updateImageField(id, image);
    }

    @Operation(summary = "Deletar imagem", description = "Remove uma imagem do sistema com base no ID informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagem deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@Parameter(description = "ID da imagem", required = true) @PathVariable UUID id) {
        service.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}