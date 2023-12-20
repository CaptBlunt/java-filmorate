package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class FilmService implements FilmStorage {

    private final FilmDao filmDao;

    @Override
    public Film addFilm(Film film) {
        return filmDao.addFilm(film);
    }

    @Override
    public Film getFilmById(int filmId) {
        return filmDao.getFilmById(filmId);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    @Override
    public void deleteFilm(int filmId) {
        filmDao.deleteFilm(filmId);
    }

    @Override
    public Film changeFilm(Film film) {
        return filmDao.changeFilm(film);
    }

    public void likeFilm(Integer userId, Integer friendId) {
        filmDao.likeFilm(userId, friendId);
    }

    @Override
    public Set<Integer> getAllLikesById(Integer userId) {
        return filmDao.getAllLikesById(userId);
    }

    @Override
    public void delLike(Integer userId, Integer friendId) {
        filmDao.delLike(userId, friendId);
    }

    @Override
    public List<Film> popularFilm(Integer countFilms) {
        return filmDao.popularFilm(countFilms);
    }

}
