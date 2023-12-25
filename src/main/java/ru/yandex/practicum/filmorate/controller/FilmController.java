package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    public static final LocalDate RELEASE_FIRST_FILM = LocalDate.parse("1895-12-28"); // дата релиза первого фильма
    public static final int MAX_LENGTH_DESC = 200; // максимальная длина описания

    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") int filmId) {
        log.info("Пришёл GET запрос /films/" + filmId);
        Film film = filmService.getFilmById(filmId);
        log.info("Отправлен ответ getFilmById/films/" + filmId + " с телом : {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilm() {
        log.info("Пришёл GET запрос /films");
        List<Film> films = filmService.getAllFilms();
        log.info("Отправлен ответ getAllFilms /films с телом : {}", films);
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film newFilm) {
        log.info("Пришел Post запрос /films с телом: {}", newFilm);
        validationFilm(newFilm);
        Film savedFilm = filmService.addFilm(newFilm);
        log.info("Отправлен ответ addFilm /films с телом : {}", savedFilm);
        return savedFilm;
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") Integer filmId) {
        log.info("Пришёл DELETE запрос /films/" + filmId);
        filmService.deleteFilm(filmId);
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("Пришёл PUT запрос /films с телом: {}", film);
        validationFilm(film);
        filmService.changeFilm(film);
        log.info("Отправлен ответ changeFilm /films с телом : {}", film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("Пришёл PUT запрос /films/" + filmId + "/like/" + userId);
        filmService.likeFilm(userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void delLikeFilm(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("Пришёл DEL запрос /films/" + filmId + "/like/" + userId);
        filmService.deleteUserLike(userId, filmId);
        log.info("Пришёл ответ delLikeFilm /films/" + filmId + "/like/" + userId + " с телом : {}",
                filmService.getFilmById(filmId));
    }

    @GetMapping("/popular")
    public List<Film> popularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Пришёл GET запрос /films/popular");
        List<Film> films = filmService.popularFilm(count);
        log.info("Пришёл ответ getPopularFilm /films/popular " + count);
        return films;
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
