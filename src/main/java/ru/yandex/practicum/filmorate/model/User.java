package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@NotNull
public class User {
    private int id;
    @Email(message = "Введите корректный email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
    @NotBlank(message = "Login не может быть пустым")
    private String login;
    private String name;
    private LocalDate birthday;
}
