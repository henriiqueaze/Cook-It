package com.p5Project.Cook_It.exceptions.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetail {

    private String field;
    private String message;

}
