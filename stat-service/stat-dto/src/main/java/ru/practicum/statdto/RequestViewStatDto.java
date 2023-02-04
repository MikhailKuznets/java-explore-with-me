package ru.practicum.statdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestViewStatDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private String[] uris;
    private Boolean unique;
}
