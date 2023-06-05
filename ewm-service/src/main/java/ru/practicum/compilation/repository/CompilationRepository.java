package ru.practicum.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.compilation.model.Compilation;

public interface CompilationRepository extends PagingAndSortingRepository<Compilation, Long> {
    Page<Compilation> findAllByPinnedIs(Boolean pinned, Pageable pageable);

    Compilation findCompilationById(Long compId);
}
