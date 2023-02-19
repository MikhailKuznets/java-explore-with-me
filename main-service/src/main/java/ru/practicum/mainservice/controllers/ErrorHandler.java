package ru.practicum.mainservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.mainservice.exception.*;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final InvalidIdException e) {
        log.error("HTTP status code 404 - " + e.getMessage());
        return new ErrorResponse(e);
    }

    @ExceptionHandler({NonCanceledRequestException.class,
            NonUpdatedEventException.class,
            ParticipantLimitException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final ApiError e) {
        log.error("HTTP status code 409 - " + e.getMessage());
        return new ErrorResponse(e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnknownBookingState(final MethodArgumentTypeMismatchException e) {
        log.error("BAD REQUEST , КОД 400 - {}", e.getMessage());
//        return new ErrorResponse("Unknown " + e.getName() + ": " + e.getValue());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), "Test1", LocalDateTime.now());
        return new ErrorResponse(apiError);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateParameterException(final MethodArgumentNotValidException e) {
        log.error("КОД 400 - Ошибка валидации данных: {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), "Test2", LocalDateTime.now());

        return new ErrorResponse(apiError);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateParameterException(final MissingServletRequestParameterException e) {
        log.error("КОД 400 - Ошибка валидации данных: {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), "Test3", LocalDateTime.now());

        return new ErrorResponse(apiError);
    }

}

