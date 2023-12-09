package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final String NOT_FOUND_FILM = "Фильм с id %d не найден";
    private final Map<Integer, Film> films = new HashMap<>();
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
        Film mutableFilm = films.get(filmUpd.getId());
        if (mutableFilm == null) {
            String message = String.format(NOT_FOUND_FILM, filmUpd.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
        Set<Integer> filmsIds = mutableFilm.getLikes();
        filmUpd.setLikes(filmsIds);
        films.put(filmUpd.getId(), filmUpd);
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
        return getAllFilms()
                .stream()
                .filter(p -> p.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_FILM, filmId)));
    }
}
