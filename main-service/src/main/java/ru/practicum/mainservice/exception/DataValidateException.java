package ru.practicum.mainservice.exception;

public class DataValidateException extends RuntimeException {
    public DataValidateException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
