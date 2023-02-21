package ru.practicum.mainservice.event.validation;

import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.exception.DataValidateException;
import ru.practicum.mainservice.exception.NonUpdatedEventException;

import java.time.LocalDateTime;

public class EventValidator {
    private static final String UPDATE_EVENT_EXCEPTION_MESSAGE = "Event must not be published";

    public static void validateNewEventDto(NewEventDto newEvent) {
        String annotation = newEvent.getAnnotation();
        String description = newEvent.getDescription();
        LocalDateTime eventDate = newEvent.getEventDate();
        String title = newEvent.getTitle();

        validateAnnotation(annotation);
        validateDescription(description);
        validatePostEventDate(eventDate);
        validateTitle(title);
    }

    public static void validateAnnotation(String annotation) {
        if (annotation.isBlank()
                || annotation.length() < 20
                || annotation.length() > 2000) {
            throw new DataValidateException(UPDATE_EVENT_EXCEPTION_MESSAGE, LocalDateTime.now());
        }
    }

    public static void validateDescription(String description) {
        if (description.isBlank()
                || description.length() < 20
                || description.length() > 7000) {
            throw new DataValidateException(UPDATE_EVENT_EXCEPTION_MESSAGE, LocalDateTime.now());
        }
    }

    public static void validateTitle(String description) {
        if (description.isBlank()
                || description.length() < 3
                || description.length() > 120) {
            throw new DataValidateException(UPDATE_EVENT_EXCEPTION_MESSAGE, LocalDateTime.now());
        }
    }

    public static void validatePostEventDate(LocalDateTime eventDate) {
        LocalDateTime now = LocalDateTime.now();
        if (eventDate.isBefore(now.plusHours(1))) {
            throw new NonUpdatedEventException("The date and time for which the event is scheduled cannot be earlier" +
                    " than two hours from the current moment", LocalDateTime.now());
        }
    }

    public static void validatePatchEventDate(LocalDateTime eventDate) {
        LocalDateTime now = LocalDateTime.now();
        if (eventDate.isBefore(now.plusHours(2))) {
            throw new NonUpdatedEventException("The date and time for which the event is scheduled cannot be earlier " +
                    "than two hours from the current moment", LocalDateTime.now());
        }
    }


}
