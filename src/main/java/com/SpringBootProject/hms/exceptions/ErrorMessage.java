package com.SpringBootProject.hms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private Integer statusValue;
    private Throwable cause;
    private String error;
    private String message;
    private String path;
}
