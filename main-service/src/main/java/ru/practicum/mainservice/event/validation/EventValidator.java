package ru.practicum.mainservice.event.validation;

import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.model.UpdateEventUserState;
import ru.practicum.mainservice.exception.DataValidateException;

import java.time.LocalDateTime;

public class EventValidator {

    public void validateNewEventDto(NewEventDto newEvent) {
        String annotation = newEvent.getAnnotation();
        String description = newEvent.getDescription();
        LocalDateTime eventDate = newEvent.getEventDate();
        String title = newEvent.getTitle();

        validateAnnotation(annotation);
        validateDescription(description);
        validatePostEventDate(eventDate);
        validateTitle(title);
    }

    public void validateUpdateEventUserRequest(UpdateEventUserRequest updateEvent) {
        String annotation = updateEvent.getAnnotation();
        String description = updateEvent.getDescription();

        Long category = updateEvent.getCategory();

        LocalDateTime eventDate = updateEvent.getEventDate();

        Boolean paid = updateEvent.getPaid();
        Integer participantLimit = updateEvent.getParticipantLimit();
        Boolean requestModeration = updateEvent.getRequestModeration();
        UpdateEventUserState stateAction = updateEvent.getStateAction();

        String title = updateEvent.getTitle();

        if (annotation != null) {
            validateAnnotation(annotation);
        }
        if (description != null) {
            validateDescription(description);
        }
        if (eventDate != null) {
            validatePatchEventDate(eventDate);
        }
        if (title != null) {
            validateTitle(title);
        }
    }

    private static void validateAnnotation(String annotation) {
        if (annotation.isBlank()
                || annotation.length() < 20
                || annotation.length() > 2000) {
            throw new DataValidateException("For the requested operation the conditions are not met.");
        }
    }

    private static void validateDescription(String description) {
        if (description.isBlank()
                || description.length() < 20
                || description.length() > 7000) {
            throw new DataValidateException("For the requested operation the conditions are not met.");
        }
    }

    private static void validateTitle(String description) {
        if (description.isBlank()
                || description.length() < 3
                || description.length() > 120) {
            throw new DataValidateException("For the requested operation the conditions are not met.");
        }
    }

    private static void validatePostEventDate(LocalDateTime eventDate) {
        LocalDateTime now = LocalDateTime.now();
        if (eventDate.isBefore(now.plusHours(1))) {
            throw new DataValidateException("For the requested operation the conditions are not met.");
        }
    }

    private static void validatePatchEventDate(LocalDateTime eventDate) {
        LocalDateTime now = LocalDateTime.now();
        if (eventDate.isBefore(now.plusHours(2))) {
            throw new DataValidateException("For the requested operation the conditions are not met.");
        }
    }


}
