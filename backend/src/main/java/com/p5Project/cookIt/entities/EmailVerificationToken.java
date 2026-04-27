package com.p5Project.cookIt.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "email_verification_tokens",
        indexes = {
                @Index(name = "idx_email_verification_tokens_token", columnList = "token"), //cria indices nessas colunas, para a buscar ser mais rapida
                @Index(name = "idx_email_verification_tokens_user_id", columnList = "user_id")
        }
)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}