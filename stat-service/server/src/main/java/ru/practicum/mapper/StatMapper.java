package ru.practicum.mapper;

import ru.practicum.StatisticRequestDto;
import ru.practicum.StatisticResponseDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;

public class StatMapper {
    public static StatisticResponseDto toStatDto(Stat stat) {
        return StatisticResponseDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .build();
    }

    public static Stat toStat(StatisticRequestDto statDto) {
        return new Stat(
                statDto.getId(),
                statDto.getApp(),
                statDto.getUri(),
                statDto.getIp(),
                LocalDateTime.now());
    }
}