package com.p5Project.cookIt.dtos.responses;

import com.p5Project.cookIt.dtos.UserDTO;
import lombok.Data;

@Data
public class AuthResponse {

    private UserDTO user;

    private String token;
}