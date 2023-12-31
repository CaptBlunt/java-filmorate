/*
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void setUp() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        filmController = new FilmController(new FilmService(inMemoryFilmStorage, inMemoryUserStorage), inMemoryFilmStorage);
    }

    @Test
    public void addFilmTest() {

        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Set<Integer> likes = new TreeSet<>();

        Film film = new Film(1, "Начало", "выафа", releaseDate, 180, likes);

        filmController.addFilm(film);
        List<Film> list = filmController.getAllFilms();
        assertEquals(1, list.size(), "Фильм не добавился");

        assertEquals(film, list.get(0), "Записи не совпадают");
    }

    @Test
    public void emptyName() {
        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "", "выафа", releaseDate, 180, likes);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void movieDescription199Characters() {
        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "Начало", "111111111111111111111111111111111111111111111111111111111111" + "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" + "1111111111111111111111111111111111111111", releaseDate, 180, likes);
        int length = film.getDescription().length();
        assertEquals(199, length, "Количество символов в описании не 199");

        filmController.addFilm(film);
        List<Film> list = filmController.getAllFilms();
        assertEquals(1, list.size(), "Фильм не добавился");

        assertEquals(film, list.get(0), "Записи не совпадают");
    }

    @Test
    public void movieDescription200Characters() {
        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "Начало", "111111111111111111111111111111111111111111111111111111111111" + "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" + "11111111111111111111111111111111111111111", releaseDate, 180, likes);
        int length = film.getDescription().length();
        assertEquals(200, length, "Количество символов в описании не 200");

        filmController.addFilm(film);
        List<Film> list = filmController.getAllFilms();
        assertEquals(1, list.size(), "Фильм не добавился");

        assertEquals(film, list.get(0), "Записи не совпадают");
    }

    @Test
    public void movieDescription201Characters() {
        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "Начало", "111111111111111111111111111111111111111111111111111111111111" + "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" + "111111111111111111111111111111111111111111", releaseDate, 180, likes);
        int length = film.getDescription().length();
        assertEquals(201, length, "Количество символов в описании не 201");

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        List<Film> list = filmController.getAllFilms();
        assertEquals(0, list.size(), "Фильм добавился");
    }

    @Test
    public void filmRelease1895_12_27() {
        LocalDate releaseDate = LocalDate.parse("1895-12-27");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "Нет такого", "ываыва", releaseDate, 180, likes);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        List<Film> list = filmController.getAllFilms();
        assertEquals(0, list.size(), "Фильм добавился");
    }

    @Test
    public void filmRelease1895_12_28() {
        LocalDate releaseDate = FilmController.RELEASE_FIRST_FILM;
        Set<Integer> id = new TreeSet<>();
        Film film = new Film(1, "Прибытие поезда на вокзал Ла-Сьота", "ываыва", releaseDate, 180, id);

        filmController.addFilm(film);
        List<Film> list = filmController.getAllFilms();
        assertEquals(1, list.size(), "Фильм не добавился");
        assertEquals(film, list.get(0), "Записи не совпадают");
    }

    @Test
    public void filmRelease1895_12_29() {
        LocalDate releaseDate = LocalDate.parse("1895-12-29");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "Ааыаваы", "ываыва", releaseDate, 180, likes);

        filmController.addFilm(film);
        List<Film> list = filmController.getAllFilms();
        assertEquals(1, list.size(), "Фильм не добавился");
        assertEquals(film, list.get(0), "Записи не совпадают");
    }

    @Test
    public void movieDurationMinus1() {
        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "Начало", "ываыва", releaseDate, -1, likes);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        List<Film> list = filmController.getAllFilms();
        assertEquals(0, list.size(), "Фильм добавился");
    }

    @Test
    public void movieDurationZero() {
        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "Начало", "ываыва", releaseDate, 0, likes);

        filmController.addFilm(film);

        List<Film> list = filmController.getAllFilms();
        assertEquals(1, list.size(), "Фильм не добавился");
    }

    @Test
    public void movieDuration1() {
        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Set<Integer> likes = new TreeSet<>();
        Film film = new Film(1, "Начало", "ываыва", releaseDate, 1, likes);

        filmController.addFilm(film);

        List<Film> list = filmController.getAllFilms();
        assertEquals(1, list.size(), "Фильм не добавился");
    }

}*/
