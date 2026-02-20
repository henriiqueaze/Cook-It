package com.p5Project.Cook_It.models.dtos.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    private String displayName;
    private String avatarUrl;
}
