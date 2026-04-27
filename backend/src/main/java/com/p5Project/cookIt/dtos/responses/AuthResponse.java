package com.p5Project.cookIt.dtos.responses;

import com.p5Project.cookIt.dtos.UserDTO;

public record AuthResponse(UserDTO user, String token) {
}