package ru.practicum.mainservice.compilation.dto;

import lombok.Data;
import ru.practicum.mainservice.event.dto.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
