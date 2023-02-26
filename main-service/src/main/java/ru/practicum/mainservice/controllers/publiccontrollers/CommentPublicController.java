package ru.practicum.mainservice.controllers.publiccontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommentDto> getComment(@PathVariable @Positive long commentId) {
        log.info("GET-request was received at 'comments/{}' . " +
                        "Get COMMENT with commentId = {}.",
                commentId, commentId);
        return new ResponseEntity<>(commentService.getCommentByPublic(commentId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable @Positive long eventId) {
        log.info("GET-request was received at '/events/{}/comments' . " +
                        "Get all COMMENTS to the EVENT with eventId = {} .",
                eventId, eventId);
        return new ResponseEntity<>(commentService.getEventCommentsByPublic(eventId), HttpStatus.OK);
    }
}
