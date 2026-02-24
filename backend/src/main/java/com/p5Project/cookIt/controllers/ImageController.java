package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.controllers.docs.ImageControllerDocs;
import com.p5Project.cookIt.models.dtos.ImageDTO;
import com.p5Project.cookIt.services.ImageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/image")
@Tag(name = "Image", description = "Endpoints para gerenciamento de imagens das receitas")
public class ImageController implements ImageControllerDocs {

    @Autowired
    private ImageService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ImageDTO findImageById(@Parameter(description = "ID da imagem", required = true) @PathVariable UUID id) {
        return service.findImageById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<ImageDTO>>> findAllImages(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "12") Integer size, @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(service.findAllImages(pageable));
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