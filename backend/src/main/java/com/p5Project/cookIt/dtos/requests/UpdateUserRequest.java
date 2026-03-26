package com.p5Project.cookIt.dtos.requests;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String photo;
}