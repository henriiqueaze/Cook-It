package com.p5Project.cookIt.models.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "recipe", nullable = false)
    private Recipe recipe;

    @Column(name = "user", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "createdAt", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}

