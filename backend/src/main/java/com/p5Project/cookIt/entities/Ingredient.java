package com.p5Project.cookIt.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
}