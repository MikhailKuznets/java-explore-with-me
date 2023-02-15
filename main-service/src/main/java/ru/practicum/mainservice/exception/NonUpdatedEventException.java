package ru.practicum.mainservice.exception;

public class NonUpdatedEventException extends RuntimeException {
    public NonUpdatedEventException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}