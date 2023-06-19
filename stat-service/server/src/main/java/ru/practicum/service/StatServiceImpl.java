package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistic.dto.StatisticRequestDto;
import ru.practicum.statistic.dto.StatisticResponseDto;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;
import ru.practicum.model.StatResult;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    @Transactional
    public StatisticResponseDto createQuery(StatisticRequestDto statisticRequestDto) {
        Stat stats = StatMapper.toStat(statisticRequestDto);
        Stat createdStat = statRepository.save(stats);
        return StatMapper.toStatDto(createdStat);
    }

    @Override
    public List<StatisticResponseDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<StatResult> result = uris == null && unique ? statRepository.findDistinctAndTimeStampBetween(start, end) :
                (uris == null ? statRepository.findAndTimeStampBetween(start, end) : (unique ?
                        statRepository.findDistinctAndTimeStampBetweenAndUriIn(start, end, uris) :
                        statRepository.findAndTimeStampBetweenAndUriIn(start, end, uris)));
        return result.stream()
                .map(a -> new StatisticResponseDto(a.getApp(), a.getUri(), a.getHit()))
                .sorted(Comparator.comparingLong(StatisticResponseDto::getHits).reversed())
                .collect(Collectors.toList());
    }
}
