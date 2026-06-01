package com.p5Project.cookIt.repository;

import com.p5Project.cookIt.entities.BannedWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannedWordRepository extends JpaRepository<BannedWord, String> {

    Optional<BannedWord> findByTermIgnoreCase(String term);
}