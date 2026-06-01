package com.p5Project.cookIt.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "banned_words")
public class BannedWord extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String term;

    @Column(nullable = false)
    private boolean appliesToRecipes = true;

    @Column(nullable = false)
    private boolean appliesToIngredients = true;

    @Column(nullable = false)
    private boolean appliesToComments = true;
}