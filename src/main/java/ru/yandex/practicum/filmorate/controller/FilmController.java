package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    public static final LocalDate RELEASE_FIRST_FILM = LocalDate.parse("1895-12-28"); // дата релиза первого фильма
    public static final int MAX_LENGTH_DESC = 200; // максимальная длина описания

    private final FilmService filmService;
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(FilmService filmService, InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmService = filmService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Пришел Post запрос /films с телом: {}", film);
        validationFilm(film);
        inMemoryFilmStorage.addFilm(film);
        log.info("Отправлен ответ addFilm /films с телом : {}", film);
        return film;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("Пришёл PUT запрос /films с телом: {}", film);
        validationFilm(film);
        inMemoryFilmStorage.changeFilm(film);
        log.info("Отправлен ответ changeFilm /films с телом : {}", film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("Пришёл PUT запрос /films/" + filmId + "/like/" + userId);
        Film film = filmService.likeFilm(userId, filmId);
        log.info("Отправлен ответ likeFilm /films/" + filmId + "/like/" + userId + " с телом : {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Пришёл GET запрос /films");
        List<Film> films = inMemoryFilmStorage.getAllFilms();
        log.info("Отправлен ответ getAllFilms /films с телом : {}", films);
        return films;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilm(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Пришёл GET запрос /films/popular");
        List<Film> films = filmService.getPopularFilm(count);
        log.info("Пришёл ответ getPopularFilm /films/popular " + count);
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer filmId) {
        log.info("Пришёл GET запрос /films/" + filmId);
        Film film = inMemoryFilmStorage.findFilmById(filmId);
        log.info("Отправлен ответ getFilmById/films/" + filmId + " с телом : {}", film);
        return film;
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable("filmId") Integer filmId) {
        log.info("Пришёл DEL запрос /films/" + filmId);
        inMemoryFilmStorage.deleteFilm(filmId);
        log.info("Отправлен ответ deleteFilm/films/" + filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film delLikeFilm(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("Пришёл DEL запрос /films/" + filmId + "/like/" + userId);
        Film film = filmService.delLikeFilm(userId, filmId);
        log.info("Пришёл ответ delLikeFilm /films/" + filmId + "/like/" + userId + " с телом : {}", film);
        return film;
    }

    private void validationFilm(Film film) {
        String message;
        if (film.getName().isEmpty()) {
            message = "У фильма отсутствует название";
            log.error(message);
            throw new ValidationException(message);
        } else if (film.getDescription().length() > MAX_LENGTH_DESC) {
            message = "Максимально допустимое количество символов в описание: 200. " + "Количество символов в описании текущего фильма " + film.getDescription().length();
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
