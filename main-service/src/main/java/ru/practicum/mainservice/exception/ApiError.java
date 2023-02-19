package ru.practicum.mainservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError extends RuntimeException {
    public HttpStatus status;
    public String message;
    public String reason;
    public LocalDateTime timestamp;
}


