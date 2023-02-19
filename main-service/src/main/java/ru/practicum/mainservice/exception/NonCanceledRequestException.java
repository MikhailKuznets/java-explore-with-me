package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class NonCanceledRequestException extends ApiError {
    public NonCanceledRequestException(String message, LocalDateTime timestamp) {
        this.status = HttpStatus.CONFLICT;
        this.reason = "For the requested operation the conditions are not met.";
        this.message = "The category is not empty";
        this.timestamp = timestamp.withNano(0);
    }
}