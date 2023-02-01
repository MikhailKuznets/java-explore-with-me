package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statserver.mapper.RequestHitDto2;
import ru.practicum.statserver.mapper.StatMapper;
import ru.practicum.statserver.model.Hit;
import ru.practicum.statserver.repository.StatRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatService {
    private final StatRepository statRepository;

    public Hit saveHit(RequestHitDto2 requestHitDto) {
        Hit hit = StatMapper.INSTANCE.toHit(requestHitDto);
        log.error(hit.toString());
        return statRepository.save(hit);
    }
}
