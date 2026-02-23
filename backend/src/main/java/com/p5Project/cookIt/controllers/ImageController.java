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
public class ImageController implements com.p5Project.cookIt.controllers.docs.ImageControllerDocs {

    @Autowired
    private ImageService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ImageDTO findImageById(@Parameter(description = "ID da imagem", required = true) @PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<ImageDTO> findAllImages() {
        return service.findAll();
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ImageDTO createImage(@RequestBody(description = "Objeto contendo os dados da imagem", required = true, content = @Content(schema = @Schema(implementation = ImageDTO.class))) @org.springframework.web.bind.annotation.RequestBody ImageDTO image) {
        return service.createImage(image);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ImageDTO updateImage(@RequestBody(description = "Objeto contendo os dados atualizados da imagem", required = true, content = @Content(schema = @Schema(implementation = ImageDTO.class))) @org.springframework.web.bind.annotation.RequestBody ImageDTO image) {
        return service.updateImage(image);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ImageDTO updateImageField(@Parameter(description = "ID da imagem", required = true) @PathVariable UUID id,
                                     @RequestBody(description = "Objeto contendo apenas os campos que serão atualizados", required = true, content = @Content(schema = @Schema(implementation = ImageDTO.class))) @org.springframework.web.bind.annotation.RequestBody ImageDTO image) {
        return service.updateImageField(id, image);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> deleteImage(@Parameter(description = "ID da imagem", required = true) @PathVariable UUID id) {
        service.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}