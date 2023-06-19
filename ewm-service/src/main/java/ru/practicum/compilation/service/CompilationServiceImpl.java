package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ObjectNotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(new Compilation(), newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllByIdIn(newCompilationDto.getEvents())));
        }
        if (newCompilationDto.getTitle() == null || newCompilationDto.getTitle().isBlank()) {
            throw new BadRequestException("Пустое название подборки");
        }

        Compilation createdCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(createdCompilation);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long id, NewCompilationDto newCompilationDto) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(id);
        if (compilationOptional.isEmpty()) {
            throw new ObjectNotFoundException("Подборка не найдена " + id);
        }

        Compilation compilation = compilationOptional.get();
        CompilationMapper.toCompilation(compilation, newCompilationDto);
        compilation.setId(id);
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllByIdIn(newCompilationDto.getEvents())));
        }

        return CompilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    @Override
    public void removeCompilation(Long id) {
        compilationRepository.deleteAllById(Collections.singleton(id));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compilationRepository.findAllByPinnedIs(pinned, pageable).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findCompilationById(compId));
    }
}
