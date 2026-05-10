package com.p5Project.cookIt.entities;

import jakarta.persistence.*;
@Entity
@Table(
        name = "email_verification_tokens",
        indexes = {
                @Index(name = "idx_email_verification_tokens_token", columnList = "token"), //cria indices nessas colunas, para a buscar ser mais rapida
                @Index(name = "idx_email_verification_tokens_user_id", columnList = "user_id")
        }
)
public class EmailVerificationToken extends AbstractTokenEntity {
}