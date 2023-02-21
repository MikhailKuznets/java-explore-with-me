package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ParticipantLimitException extends ApiError {
    public ParticipantLimitException(String message, LocalDateTime timestamp) {
        this.status = HttpStatus.CONFLICT;
        this.reason = "For the requested operation the conditions are not met.";
        this.message = message;
        this.timestamp = timestamp.withNano(0);
    }
}
