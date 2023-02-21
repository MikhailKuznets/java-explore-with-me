package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class NotPendingStatusException extends ApiError {
    public NotPendingStatusException(String message, LocalDateTime timestamp) {
        this.status = HttpStatus.BAD_REQUEST;
        this.reason = "Incorrectly made request.";
        this.message = message;
        this.timestamp = timestamp.withNano(0);
    }
}
