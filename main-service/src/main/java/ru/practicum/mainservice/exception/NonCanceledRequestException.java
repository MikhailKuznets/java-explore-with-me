package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class NonCanceledRequestException extends ApiError {
    public NonCanceledRequestException(String message, LocalDateTime timestamp) {
        this.status = HttpStatus.CONFLICT;
        this.reason = "Integrity constraint has been violated.";
        this.message = message;
        this.timestamp = timestamp.withNano(0);
    }
}
