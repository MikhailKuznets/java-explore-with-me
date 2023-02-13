package ru.practicum.mainservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidIdException(final InvalidIdException e) {
        log.error("HTTP status code 404 - invalid ID = {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}


class ErrorResponse {
    private final String error;
    public ErrorResponse(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }
}