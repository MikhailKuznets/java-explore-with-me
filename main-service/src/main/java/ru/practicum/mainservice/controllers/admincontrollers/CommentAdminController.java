package ru.practicum.mainservice.controllers.admincontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.service.CommentService;
import ru.practicum.mainservice.controllers.admincontrollers.parameters.CommentAdminRequestParameters;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/comments")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Collection<CommentDto>> getCommentsForAdmin(
            @RequestParam(required = false) String text,
            @RequestParam(defaultValue = "", required = false) List<Long> users,
            @RequestParam(defaultValue = "", required = false) List<Long> events,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {

        CommentAdminRequestParameters parameters = CommentAdminRequestParameters.builder()
                .text(text)
                .userIds(users)
                .eventIds(events)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();

        log.info("GET-request was received at 'admin/comments' . " +
                "Get all COMMENT with parameters = {}.", parameters);
        return new ResponseEntity<>(commentService.getCommentForAdmin(parameters, from, size),
                HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable @Positive Long commentId) {
        log.info("DELETE-request was received at 'admin/comments/{}' . " +
                "Delete a COMMENT with commentID = {}.", commentId, commentId);
        commentService.deleteCommentById(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
