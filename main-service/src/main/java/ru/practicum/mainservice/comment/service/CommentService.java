package ru.practicum.mainservice.comment.service;

import org.springframework.stereotype.Service;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.dto.UpdateCommentDto;
import ru.practicum.mainservice.controllers.admincontrollers.parameters.CommentAdminRequestParameters;
import ru.practicum.mainservice.controllers.privatecontroller.parameters.CommentPrivateRequestParameters;

import java.util.Collection;

@Service
public class CommentService {
    public void deleteCommentById(Long commentId) {
    }

    public Collection<CommentDto> getCommentForAdmin(Long userId) {
        return null;
    }

    public CommentDto createCommentOnEvent(Long userId, Long eventId, NewCommentDto newCommentDto) {
        return null;
    }

    public Collection<CommentDto> getAllUserComments(Long userId) {
        return null;
    }

    public CommentDto updateUserComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {
        return null;
    }

    public void deleteUserCommentById(Long userId, Long commentId) {

    }

    public Collection<CommentDto> getCommentForAdmin(CommentAdminRequestParameters parameters,
                                                     Integer from,
                                                     Integer size) {
        return null;
    }

    public Collection<CommentDto> getCommentForUser(Long userId,
                                                    CommentPrivateRequestParameters parameters,
                                                    Integer from,
                                                    Integer size) {
        return null;
    }
}
