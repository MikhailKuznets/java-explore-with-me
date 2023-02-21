package ru.practicum.mainservice.comment.service;

import org.springframework.stereotype.Service;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.dto.UpdateCommentDto;

import javax.validation.constraints.Positive;
import java.util.Collection;

@Service
public class CommentService {
    public void deleteCommentById(Long commentId) {
    }

    public Collection<CommentDto> getAllUsersCommentForAdmin(Long userId) {
        return null;
    }

    public CommentDto createCommentOnEvent(Long userId, Long eventId, NewCommentDto newCommentDto) {
        return null;
    }

    public Collection<CommentDto> getAllUserComments(Long userId) {
        return null;
    }

    public CommentDto updateUserComment(Long userId, @Positive Long commentId, UpdateCommentDto updateCommentDto) {
        return null;
    }

    public void deleteUserCommentById(Long userId, Long commentId) {

    }
}
