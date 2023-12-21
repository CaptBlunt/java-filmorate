package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);

    Film changeFilm(Film film);

    void deleteFilm(int filmId);

    List<Film> getAllFilms();

    Film getFilmById(int id);

    void likeFilm(Integer userId, Integer filmId);

    Set<Integer> getAllLikesById(Integer userId);

    void deleteUserLike(Integer userId, Integer filmId);

    List<Film> popularFilm(Integer countFilms);
}
