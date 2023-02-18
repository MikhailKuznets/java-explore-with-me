package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.InvalidIdException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
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
        List<Event> events = Collections.EMPTY_LIST;
        if (!eventIds.isEmpty()) {
            events = eventIds.stream()
                    .map(this::findEvent)
                    .collect(Collectors.toList());
        }
        Compilation newCompilation = compilationMapper.toCompilation(newCompilationDto);
        newCompilation.setEvents(events);
        return compilationMapper.toCompilationDto(compilationRepository.save(newCompilation));
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation updatedCompilation = findCompilation(compId);

        List<Long> newEventIds = updateCompilationRequest.getEvents();
        Boolean newPinned = updateCompilationRequest.getPinned();
        String newTitle = updateCompilationRequest.getTitle();

        if (newPinned != null) {
            updatedCompilation.setPinned(newPinned);
        }
        if (newTitle != null) {
            updatedCompilation.setTitle(newTitle);
        }
        if (newEventIds != null) {
            List<Event> newEvents = newEventIds.stream()
                    .map(this::findEvent)
                    .collect(Collectors.toList());
            updatedCompilation.setEvents(newEvents);
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(updatedCompilation));
    }


    public CompilationDto getPublicCompilationById(Long compId) {
        Compilation compilation = findCompilation(compId);
        return compilationMapper.toCompilationDto(compilation);
    }


    public Collection<CompilationDto> getPublicAllCompilation(Boolean pinned) {
        Collection<Compilation> compilations = Collections.emptyList();
        if (pinned != null) {
            compilations = compilationRepository.findByPinned(pinned);
        } else {
            compilations = compilationRepository.findAll();
        }
        return compilations.stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    public void deleteCompilationById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    private Compilation findCompilation(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            throw new InvalidIdException("Compilation", compId, LocalDateTime.now());
        });
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new InvalidIdException("Event", eventId, LocalDateTime.now());
        });
    }
}
