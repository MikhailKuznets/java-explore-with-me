package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.InvalidIdException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;


    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Long> eventIds = newCompilationDto.getEvents();
        List<Event> events = eventIds.stream()
                .map(this::findEvent)
                .collect(Collectors.toList());

        Compilation newCompilation = compilationMapper.toCompilation(newCompilationDto);
        newCompilation.setEvents(events);
        if (newCompilation.getPinned() == null) {
            newCompilation.setPinned(false);
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(newCompilation));
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new InvalidIdException("Event", eventId, LocalDateTime.now());
        });
    }
}
