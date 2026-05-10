package com.p5Project.cookIt.entities;

import jakarta.persistence.*;
@Entity
@Table(
        name = "password_reset_tokens",
        indexes = {
                @Index(name = "idx_password_reset_tokens_token", columnList = "token"),
                @Index(name = "idx_password_reset_tokens_user_id", columnList = "user_id")
        }
)
public class PasswordResetToken extends AbstractTokenEntity {
}