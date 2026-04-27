package com.p5Project.cookIt.dtos.responses;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String error, String path) {
}