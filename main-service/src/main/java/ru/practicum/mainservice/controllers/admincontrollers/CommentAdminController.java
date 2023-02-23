package ru.practicum.mainservice.controllers.admincontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/comments")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping("/{userId}")
    public ResponseEntity<Collection<CommentDto>> getAllUserCommentsForAdmin(@PathVariable @Positive Long userId) {
        log.info("GET-request was received at 'admin/comments/{}' . " +
                "Get all COMMENT by USER with userID = {}.", userId, userId);
        return new ResponseEntity<>(commentService.getAllUsersCommentForAdmin(userId),
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
