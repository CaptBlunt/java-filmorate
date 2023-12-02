package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final String NOT_FOUND_FILM = "Фильм с id %d не найден";
    private int generatorId = 0;

    @Override
    public Film addFilm(Film film) {
        film.setId(++generatorId);
        film.setLikes(new TreeSet<>());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film changeFilm(Film filmUpd) {
        if (films.containsKey(filmUpd.getId())) {
            Film mutableFilm = films.get(filmUpd.getId());
            Set<Integer> likes = mutableFilm.getLikes();
            filmUpd.setLikes(likes);
            films.put(filmUpd.getId(), filmUpd);
        } else {
            String message = String.format(NOT_FOUND_FILM, filmUpd.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
        return filmUpd;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void deleteFilm(int filmId) {
        if (!films.containsKey(filmId)) {
            String message = String.format(NOT_FOUND_FILM, filmId);
            log.error(message);
            throw new NotFoundException(message);
        }
        films.remove(filmId);
    }

    public Film findFilmById(Integer filmId) {
        return getAllFilms().stream().filter(p -> p.getId() == filmId).findFirst().orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_FILM, filmId)));
    }
}
