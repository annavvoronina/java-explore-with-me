package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "имя не должно быть пустым")
    private String name;
    @Email(message = "не корректный e-mail")
    @NotBlank
    private String email;
}
