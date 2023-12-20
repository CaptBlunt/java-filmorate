package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmDao {
    Film addFilm(Film film);

    Film changeFilm(Film film);

    void deleteFilm(int filmId);

    List<Film> getAllFilms();

    Film getFilmById(int id);

    void likeFilm(Integer userId, Integer filmId);

    Set<Integer> getAllLikesById(Integer userId);

    void delLike(Integer userId, Integer filmId);

    List<Film> popularFilm(Integer countFilms);
}
