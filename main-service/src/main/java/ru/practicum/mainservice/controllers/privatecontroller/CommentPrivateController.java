package ru.practicum.mainservice.controllers.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.dto.UpdateCommentDto;
import ru.practicum.mainservice.comment.service.CommentService;
import ru.practicum.mainservice.controllers.parameters.comment.BaseCommentRequestParameters;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> createCommentToEvent(@PathVariable @Positive Long userId,
                                                           @PathVariable @Positive Long eventId,
                                                           @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST-request was received at 'users/{}/events/{}/comments' . " +
                        "USER with userID = {} on EVENT with eventID = {}: create new COMMENT: {}.",
                userId, eventId, userId, eventId, newCommentDto);
        return new ResponseEntity<>(commentService.createComment(userId, eventId, newCommentDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long commentId) {
        log.info("GET-request was received at 'users/{}/comments/{}' . " +
                        "Get COMMENT with commentId = {} by USER with userId = {} .",
                userId, commentId, commentId, userId);
        return new ResponseEntity<>(commentService.getCommentById(userId, commentId),
                HttpStatus.OK);
    }

    @GetMapping("/{userId}/comments/own")
    public ResponseEntity<Collection<CommentDto>> getAllUserComments(@PathVariable @Positive Long userId) {
        log.info("GET-request was received at 'users/{}/comments' . " +
                        "Get all own COMMENTS by USER with userId = {} .",
                userId, userId);
        return new ResponseEntity<>(commentService.getAllUserComments(userId),
                HttpStatus.OK);
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<Collection<CommentDto>> getCommentsForUser(
            @PathVariable @Positive Long userId,
            @RequestParam(required = false) String text,
            @RequestParam(defaultValue = "", required = false) List<Long> events,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {

        BaseCommentRequestParameters parameters = BaseCommentRequestParameters.builder()
                .text(text)
                .eventIds(events)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();

        log.info("GET-request was received at 'users/{}/comments' . " +
                "Get for USER with userID all COMMENT with parameters = {}.", userId, parameters);
        return new ResponseEntity<>(commentService.getCommentForUser(userId, parameters, from, size),
                HttpStatus.OK);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateUserComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long commentId,
            @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        log.info("PATCH-request was received at 'users/{}/comments/{}' . " +
                        "Patch a COMMENT with commentID = {}, from USER with userID = {}. New Data = {} .",
                userId, commentId, commentId, userId, updateCommentDto);
        return new ResponseEntity<>(commentService.updateUserComment(userId, commentId, updateCommentDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteUserCommentById(@PathVariable @Positive Long userId,
                                                      @PathVariable @Positive Long commentId) {
        log.info("DELETE-request was received at 'users/{}/comments/{}' . " +
                        "Delete COMMENT with commentID = {} by USER with UserId = {}",
                userId, commentId, commentId, userId);
        commentService.deleteUserCommentById(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
