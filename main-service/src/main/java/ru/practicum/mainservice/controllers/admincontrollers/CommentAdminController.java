package ru.practicum.mainservice.controllers.admincontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.UpdateCommentDto;
import ru.practicum.mainservice.comment.service.CommentService;
import ru.practicum.mainservice.controllers.parameters.comment.AdminCommentRequestParameters;

import javax.validation.Valid;
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
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentDto> getCommentsForAdmin(
            @RequestParam(required = false) String text,
            @RequestParam(defaultValue = "", required = false) List<Long> users,
            @RequestParam(defaultValue = "", required = false) List<Long> events,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {

        AdminCommentRequestParameters parameters = new AdminCommentRequestParameters(text,
                users,
                events,
                rangeStart,
                rangeEnd);

        log.info("GET-request was received at 'admin/comments' . " +
                "Get all COMMENT with parameters = {}.", parameters);
        return commentService.getCommentForAdmin(parameters, from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommentDto deleteComment(@PathVariable @Positive long commentId) {
        log.info("DELETE-request was received at 'admin/comments/{}' . " +
                "Delete by admin COMMENT with commentID = {}.", commentId, commentId);
        return commentService.deleteCommentByAdmin(commentId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@RequestBody @Valid UpdateCommentDto updateCommentDto) {
        log.info("PATCH-request was received at 'admin/comments' . " +
                        "Patch by admin COMMENT, new Data = {} .",
                updateCommentDto);
        return commentService.updateCommentByAdmin(updateCommentDto);
    }
}