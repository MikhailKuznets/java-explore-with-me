package ru.practicum.statserver.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHitDto2 {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime created;
}
