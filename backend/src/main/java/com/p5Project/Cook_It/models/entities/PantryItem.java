package com.p5Project.Cook_It.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pantry_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PantryItem {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    private Double quantity;

    private String unit;

    @CreationTimestamp
    @Column(name = "added_at", updatable = false)
    private Instant addedAt;

    @UpdateTimestamp
    @Column(name = "expires_at")
    private Instant expiresAt;
}