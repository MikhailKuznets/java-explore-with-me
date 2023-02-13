package ru.practicum.mainservice.exceptions;

public class InvalidIdException extends RuntimeException {

    public InvalidIdException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}