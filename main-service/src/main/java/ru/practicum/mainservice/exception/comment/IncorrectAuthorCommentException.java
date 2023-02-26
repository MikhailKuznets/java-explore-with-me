package ru.practicum.mainservice.exception.comment;

import org.springframework.http.HttpStatus;
import ru.practicum.mainservice.exception.ApiError;

import java.time.LocalDateTime;

public class IncorrectAuthorCommentException extends ApiError {
    public IncorrectAuthorCommentException(String message, LocalDateTime timestamp) {
        this.status = HttpStatus.CONFLICT;
        this.reason = "For the requested operation the conditions are not met.";
        this.message = message;
        this.timestamp = timestamp.withNano(0);
    }
}
