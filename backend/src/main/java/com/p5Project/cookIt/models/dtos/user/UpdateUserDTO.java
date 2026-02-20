package com.p5Project.cookIt.models.dtos.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    private String displayName;
    private String avatarUrl;
}
