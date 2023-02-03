package ru.practicum.statdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestHitDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime created;
}
