package com.p5Project.cookIt.exceptions.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestErrorMessage {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String errorCode;
    private String message;
    private String path;
    private String method;
    private String traceId;
    private List<ErrorDetail> details;
}


