package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @Size(max = 50, message = "Слишком длинное название категории")
    @NotBlank
    private String name;
}
