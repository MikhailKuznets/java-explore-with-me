package ru.practicum.statdto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestHitDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime created;
}
