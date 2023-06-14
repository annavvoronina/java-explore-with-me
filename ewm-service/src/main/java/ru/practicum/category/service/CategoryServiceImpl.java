package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category existingCategory = categoryRepository.findByName(newCategoryDto.getName());

        if (existingCategory != null) {
            throw new ConflictException("Категория с таким именем уже существует");
        }

        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Категория не найдена " + id));

        CategoryMapper.toCategory(category, categoryDto);
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        getCategoryById(id);
        Optional<Event> event = eventRepository.findByCategoryId(id);
        if (event.isEmpty()) {
            categoryRepository.deleteById(id);
        } else {
            throw new ConflictException("У категории " + id + "есть события");
        }
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Категория не найдена " + id));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategory(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return CategoryMapper.toListDto(categoryRepository.findAll(pageable).getContent());
    }
}
