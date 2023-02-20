package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class CategoryIsNotEmptyException extends ApiError {
    public CategoryIsNotEmptyException(LocalDateTime timestamp) {
        this.status = HttpStatus.CONFLICT;
        this.reason = "Integrity constraint has been violated.";
        this.message = "The category is not empty";
        this.timestamp = timestamp.withNano(0);
    }

}
