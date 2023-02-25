package ru.practicum.mainservice.controllers.publiccontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getComment(@PathVariable @Positive long commentId) {
        log.info("GET-request was received at 'comments/{}' . " +
                        "Get COMMENT with commentId = {}.",
                commentId, commentId);
        return commentService.getCommentByPublic(commentId);
    }

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getComments(@PathVariable @Positive long eventId) {
        log.info("GET-request was received at '/events/{eventId}/comments' . " +
                        "Get all COMMENTS to the EVENT with eventId = {} .",
                eventId, eventId);
        return commentService.getEventCommentsByPublic(eventId);
    }
}
