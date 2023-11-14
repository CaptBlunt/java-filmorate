package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@NotNull
public class Film {
    private int id;
    @NotBlank(message = "У фильма отсутствует название")
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
