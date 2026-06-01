package com.p5Project.cookIt.mappers;

import com.p5Project.cookIt.dtos.BannedWordDTO;
import com.p5Project.cookIt.entities.BannedWord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BannedWordMapper {

    @Mapping(target = "createdAt", expression = "java(bannedWord.getCreatedAt() != null ? bannedWord.getCreatedAt().toString() : null)")
    BannedWordDTO toDTO(BannedWord bannedWord);

    List<BannedWordDTO> toDTOList(List<BannedWord> bannedWords);
}