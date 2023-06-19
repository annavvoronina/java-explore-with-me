package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.statistic.dto.StatisticRequestDto;
import ru.practicum.statistic.dto.StatisticResponseDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;

@UtilityClass
public class StatMapper {
    public static StatisticResponseDto toStatDto(Stat stat) {
        return StatisticResponseDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .build();
    }

    public Stat toStat(StatisticRequestDto statDto) {
        return new Stat(
                statDto.getId(),
                statDto.getApp(),
                statDto.getUri(),
                statDto.getIp(),
                LocalDateTime.now().withNano(0));
    }
}
