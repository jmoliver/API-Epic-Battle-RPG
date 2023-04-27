package com.avanade.ebr.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private String message;
    private String description;
}
