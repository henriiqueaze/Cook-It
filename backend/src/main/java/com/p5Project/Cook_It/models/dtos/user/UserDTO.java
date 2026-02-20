package com.p5Project.Cook_It.models.dtos.user;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String email;
    private String displayName;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
}