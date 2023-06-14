package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "имя не должно быть пустым")
    @Size(min = 2, message = "Слишком короткое имя пользователя")
    @Size(max = 250, message = "Слишком длинное имя пользователя")
    private String name;
    @Email(message = "не корректный e-mail")
    @NotBlank
    @Size(min = 6, message = "Слишком короткий email пользователя")
    @Size(max = 254, message = "Слишком длинный email пользователя")
    private String email;
}
