package ru.practicum.mainservice.controllers.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable @Positive long userId,
                                 @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST-request was received at 'users/{}/comments' . " +
                        "USER with userID = {} create new COMMENT: {}.",
                userId, userId, newCommentDto);
        return commentService.createComment(userId, newCommentDto);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getComment(@PathVariable @Positive long userId,
                                 @PathVariable @Positive long commentId) {
        log.info("GET-request was received at 'users/{}/comments/{}' . " +
                        "Get COMMENT with commentId = {} by USER with userId = {} .",
                userId, commentId, commentId, userId);
        return commentService.getCommentById(userId, commentId);
    }

    @GetMapping("/{userId}/comments/own")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllUserComments(@PathVariable @Positive long userId) {
        log.info("GET-request was received at 'users/{}/comments/own' . " +
                        "Get all own COMMENTS by USER with userId = {} .",
                userId, userId);
        return commentService.getAllUserComments(userId);
    }

    @GetMapping("/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getComments(@PathVariable @Positive long userId,
                                        @PathVariable @Positive long eventId) {
        log.info("GET-request was received at 'users/{}/events/{}/comments' . " +
                        "Get all COMMENTS to the EVENT with eventId = {} to the USER with userId = {}.",
                userId, eventId, eventId, userId);
        return commentService.getEventComments(userId, eventId);
    }

    @GetMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentDto> getCommentsWithFilter(
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
        return commentService.getCommentForUser(userId, parameters, from, size);
    }

    @PatchMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(
            @PathVariable @Positive long userId,
            @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        log.info("PATCH-request was received at 'users/{}/comments' . " +
                        "Patch a COMMENT by USER with userID = {}. New Data = {} .",
                userId, userId, updateCommentDto);
        return commentService.updateUserComment(userId, updateCommentDto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommentDto deleteComment(@PathVariable @Positive long userId,
                                    @PathVariable @Positive long commentId) {
        log.info("DELETE-request was received at 'users/{}/comments/{}' . " +
                        "Delete COMMENT with commentID = {} by USER with UserId = {}",
                userId, commentId, commentId, userId);
        return commentService.deleteUserCommentById(userId, commentId);
    }

}
