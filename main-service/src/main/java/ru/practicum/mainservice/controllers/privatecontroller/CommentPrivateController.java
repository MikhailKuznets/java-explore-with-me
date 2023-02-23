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

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

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
        return new ResponseEntity<>(commentService.createCommentOnEvent(userId, eventId, newCommentDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<Collection<CommentDto>> getAllUserComments(@PathVariable @Positive Long userId) {
        log.info("GET-request was received at 'users/{}/comments' . Get all COMMENT by USER with userID = {}.",
                userId, userId);
        return new ResponseEntity<>(commentService.getAllUserComments(userId),
                HttpStatus.OK);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateUserComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long commentId,
            @RequestBody UpdateCommentDto updateCommentDto) {
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
