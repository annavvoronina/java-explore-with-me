package ru.practicum.service;

import ru.practicum.dto.StatisticRequestDto;
import ru.practicum.dto.StatisticResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    StatisticResponseDto createQuery(StatisticRequestDto statisticRequestDto);

    List<StatisticResponseDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
