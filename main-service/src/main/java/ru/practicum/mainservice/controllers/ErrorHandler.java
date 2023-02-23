package ru.practicum.mainservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.mainservice.exception.ApiError;
import ru.practicum.mainservice.exception.DataValidateException;
import ru.practicum.mainservice.exception.ErrorResponse;
import ru.practicum.mainservice.exception.InvalidIdException;
import ru.practicum.mainservice.exception.category.CategoryIsNotEmptyException;
import ru.practicum.mainservice.exception.comment.IncorrectAuthorCommentException;
import ru.practicum.mainservice.exception.event.NonUpdatedEventException;
import ru.practicum.mainservice.exception.request.NonCanceledRequestException;
import ru.practicum.mainservice.exception.request.NotPendingStatusException;
import ru.practicum.mainservice.exception.request.ParticipantLimitException;
import ru.practicum.mainservice.exception.request.UnCreatedRequestException;

import javax.persistence.PersistenceException;
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

    @ExceptionHandler({DataValidateException.class,
            NotPendingStatusException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final ApiError e) {
        log.error("HTTP status code 409 - " + e.getMessage());
        return new ErrorResponse(e);
    }

    @ExceptionHandler({NonCanceledRequestException.class,
            NonUpdatedEventException.class,
            ParticipantLimitException.class,
            CategoryIsNotEmptyException.class,
            UnCreatedRequestException.class,
            IncorrectAuthorCommentException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final ApiError e) {
        log.error("HTTP status code 409 - " + e.getMessage());
        return new ErrorResponse(e);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnknownBookingState(final MethodArgumentTypeMismatchException e) {
        log.error("HTTP status code 400 - {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(),
                "TIncorrectly made request.", LocalDateTime.now());
        return new ErrorResponse(apiError);
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleValidateParameterException(final PersistenceException e) {
        log.error("HTTP status code 409: {}", e.getMessage());
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, e.getMessage(),
                "Integrity constraint has been violated.", LocalDateTime.now());
        return new ErrorResponse(apiError);
    }

}

