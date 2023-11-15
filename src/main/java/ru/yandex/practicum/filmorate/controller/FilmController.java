package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    public static final LocalDate RELEASE_FIRST_FILM = LocalDate.parse("1895-12-28"); // дата релиза первого фильма
    public static final int MAX_LENGTH_DESC = 200; // максимальная длина описания
    private final Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 0;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Пришел Post запрос /films с телом: {}", film);
        validationFilm(film);
        film.setId(++generatorId);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("Пришёл PUT запрос /films с телом: {}", film);
        if (!films.containsKey(film.getId())) {
            String message = "Фильм не найден";
            log.error(message);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
        validationFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Пришёл GET запрос /films");
        return new ArrayList<>(films.values());
    }

    private void validationFilm(Film film) {
        String message;
        if (film.getName().isEmpty()) {
            message = "У фильма отсутствует название";
            log.error(message);
            throw new ValidationException(message);
        } else if (film.getDescription().length() > MAX_LENGTH_DESC) {
            message = "Максимально допустимое количество символов в описание: 200. " +
                    "Количество символов в описании текущего фильма " + film.getDescription().length();
            log.error(message);
            throw new ValidationException(message);
        } else if (film.getReleaseDate().isBefore(RELEASE_FIRST_FILM)) {
            message = "Фильмы не существовали до этой даты " + RELEASE_FIRST_FILM;
            log.error(message);
            throw new ValidationException(message);
        } else if (film.getDuration() < 0) {
            message = "Продолжительность фильма не может быть отрицательной";
            log.error(message);
            throw new ValidationException(message);
        }
    }
}
