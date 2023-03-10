package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statdto.RequestHitDto;
import ru.practicum.statdto.RequestViewStatDto;
import ru.practicum.statdto.ViewsStatsRequest;
import ru.practicum.statserver.mapper.StatMapper;
import ru.practicum.statserver.model.Hit;
import ru.practicum.statserver.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatService {
    private final StatRepository statRepository;

    public Hit saveHit(RequestHitDto requestHitDto, LocalDateTime timeStamp) {
        requestHitDto.setCreated(timeStamp);
        Hit hit = StatMapper.INSTANCE.toHit(requestHitDto);
        return statRepository.save(hit);
    }

    public Collection<ViewsStatsRequest> getStat(RequestViewStatDto requestViewStatDto) {
        LocalDateTime start = requestViewStatDto.getStart();
        LocalDateTime end = requestViewStatDto.getEnd();
        String[] uris = requestViewStatDto.getUris();
        Boolean unique = requestViewStatDto.getUnique();
        log.warn(requestViewStatDto.toString());

        if (uris == null || uris.length == 0) {
            if (unique == null || unique.equals(false)) {
                return statRepository.getStat(start, end).stream()
                        .map(StatMapper.INSTANCE::toViewStatDto)
                        .collect(Collectors.toList());
            } else {
                return statRepository.getStatUniqueIp(start, end).stream()
                        .map(StatMapper.INSTANCE::toViewStatDto)
                        .collect(Collectors.toList());
            }

        } else {
            if (unique == null || unique.equals(false)) {
                return statRepository.getStatByUri(start, end, uris).stream()
                        .map(StatMapper.INSTANCE::toViewStatDto)
                        .collect(Collectors.toList());
            } else {
                return statRepository.getStatUniqueIpByUri(start, end, uris).stream()
                        .map(StatMapper.INSTANCE::toViewStatDto)
                        .collect(Collectors.toList());
            }
        }
    }

}
