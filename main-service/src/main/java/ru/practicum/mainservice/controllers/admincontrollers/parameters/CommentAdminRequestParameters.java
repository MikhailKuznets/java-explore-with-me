package ru.practicum.mainservice.controllers.admincontrollers.parameters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentAdminRequestParameters {
    private String text;
    private List<Long> userIds;
    private List<Long> eventIds;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
