package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.IdNotFoundException;
import com.p5Project.cookIt.mappers.Mapper;
import com.p5Project.cookIt.models.dtos.PantryItemDTO;
import com.p5Project.cookIt.models.entities.PantryItem;
import com.p5Project.cookIt.repositories.PantryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PantryItemService {

    @Autowired
    private PantryItemRepository repository;

    public PantryItemDTO findPantryItemById(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));
        return Mapper.parseItem(entity, PantryItemDTO.class);
    }

    public List<PantryItemDTO> findAllPantryItems() {
        return Mapper.parseItemsList(repository.findAll(), PantryItemDTO.class);
    }

    public PantryItemDTO createPantryItem(PantryItemDTO pantryItem) {
        var entity = Mapper.parseItem(pantryItem, PantryItem.class);
        repository.save(entity);
        return Mapper.parseItem(entity, PantryItemDTO.class);
    }

    public PantryItemDTO updatePantryItem(PantryItemDTO pantryItem) {
        var entity = repository.findById(pantryItem.getId()).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(pantryItem, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, PantryItemDTO.class);
    }

    public PantryItemDTO updatePantryItemField(UUID id, PantryItemDTO pantryItem) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found!"));

        Mapper.mapNonNullFields(pantryItem, entity);
        repository.save(entity);

        return Mapper.parseItem(entity, PantryItemDTO.class);
    }

    public void deletePantryItem(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Id not found"));
        repository.delete(entity);
    }
}
