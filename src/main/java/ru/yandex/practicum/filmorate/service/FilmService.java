package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmReRating;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    public Film likeFilm(int userId, int filmId) {
        Film film = inMemoryFilmStorage.findFilmById(filmId);
        User user = inMemoryUserStorage.findUserById(userId);
        Set<Integer> usId = film.getLikes();

        if (usId.contains(userId)) {
            throw new FilmReRating(String.format("Пользователь с id %d уже оценил фильм с id %d", userId, filmId));
        }
        usId.add(userId);
        film.setLikes(usId);
        return film;
    }

    public Film delLikeFilm(int userId, int filmId) {
        Film film = inMemoryFilmStorage.findFilmById(filmId);
        User user = inMemoryUserStorage.findUserById(userId);
        Set<Integer> usId = film.getLikes();

        if (usId.contains(userId)) {
            usId.remove(userId);
            film.setLikes(usId);
        } else {
            throw new NotFoundException(String.format("Пользователь с id %d не оценивал фильм с id %d",
                    userId, filmId));
        }
        return film;
    }

    public List<Film> getPopularFilm(Integer count) {
        return inMemoryFilmStorage.getAllFilms()
                .stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
