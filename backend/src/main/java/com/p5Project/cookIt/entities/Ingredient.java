package com.p5Project.cookIt.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ingredients", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Ingredient extends AuditableEntity {

    @Column(nullable = false)
    private String name;
}