package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;

    @NotBlank(message = "У фильма отсутствует название")
    private String name;

    private List<Genre> genres = new ArrayList<>();

    private Mpa mpa;

    private int rate;

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    private int duration;

    public Film setId(Integer id) {
        this.id = id;
        return this;
    }
}
