package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class FilmDaoImpl implements FilmDao {
    private static final String NOT_FOUND_FILM = "Фильм с id %d не найден";
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final FilmGenresStorage filmGenresStorage;

    @Override
    public Film getFilmById(int filmId) {
        String sqlQuery = "SELECT * " +
                "FROM films " +
                "WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, getFilmMapper(), filmId);
        } catch (RuntimeException e) {
            String message = String.format(NOT_FOUND_FILM, filmId);
            log.error(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * " +
                "FROM films";
        return jdbcTemplate.query(sqlQuery, getFilmMapper());
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Number key = insert.executeAndReturnKey(filmToMap(film));
        film.setId((Integer) key);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String query = "INSERT INTO film_genres (film_id, genre_id) " +
                    "VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(query, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public void deleteFilm(int filmId) {
        String sqlQuery = "DELETE FROM films " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public Film changeFilm(Film filmUpd) {

        int filmId = filmUpd.getId();
        String query = "UPDATE Films " +
                "SET name=?, description=?, release_date=?, duration=?, rate =?, mpa_id=? " +
                "WHERE id=?";
        int updateResult = jdbcTemplate.update(query, filmUpd.getName(), filmUpd.getDescription(),
                filmUpd.getReleaseDate(), filmUpd.getDuration(), filmUpd.getRate(), filmUpd.getMpa().getId(), filmId);
        if (updateResult > 0) {
            log.info("Фильм с id {} обновлён", filmId);
        } else {
            throw new NotFoundException(String.format(NOT_FOUND_FILM, filmId));
        }
        if (!filmUpd.getGenres().isEmpty()) {
            String querySql = "DELETE FROM film_genres " +
                    "WHERE film_id =?";
            jdbcTemplate.update(querySql, filmId);
            String insertGenreQuery = "INSERT INTO film_genres (film_id, genre_id) " +
                    "VALUES (?, ?)";
            filmUpd.setGenres(filmUpd.getGenres()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList()));
            for (Genre genre : filmUpd.getGenres()) {
                jdbcTemplate.update(insertGenreQuery, filmId, genre.getId());
            }
        } else {
            String querySql = "DELETE FROM film_genres " +
                    "WHERE film_id =?";
            jdbcTemplate.update(querySql, filmId);
        }
        return filmUpd;
    }

    @Override
    public void likeFilm(Integer userId, Integer filmId) {
        if (userId <= 0 || filmId <= 0) {
            String message = String.format(NOT_FOUND_FILM, filmId);
            throw new NotFoundException(message);
        }
        String sqlQuery = "INSERT INTO user_likes(user_id, film_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public Set<Integer> getAllLikesById(Integer userId) {
        String sql = "SELECT user_id " +
                "FROM user_likes " +
                "WHERE film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId);
        Set<Integer> likes = new HashSet<>();
        while (sqlRowSet.next()) {
            likes.add(sqlRowSet.getInt("user_id"));
        }
        return likes;
    }

    private RowMapper<Film> getFilmMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("id"));

            film.setName(rs.getString("name"));

            film.setDescription(rs.getString("description"));

            film.setReleaseDate(rs.getDate("release_date").toLocalDate());

            film.setDuration(rs.getInt("duration"));

            film.setMpa(mpaStorage.getMpaById(rs.getInt("mpa_id")));

            film.setGenres(filmGenresStorage.getGenres(film.getId()));

            return film;
        };
    }

    @Override
    public List<Film> popularFilm(Integer countFilms) {
        String query = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, " +
                "f.mpa_id, mpa.name, COUNT(user_likes.user_id) AS likes "
                + "FROM films f " + "LEFT JOIN user_likes ON f.id = user_likes.film_id "
                + "JOIN mpa ON f.mpa_id = mpa.id "
                + "GROUP BY f.id "
                + "ORDER BY likes DESC "
                + "LIMIT ?";

        RowMapper<Film> filmRowMapper = getFilmMapper();
        return jdbcTemplate.query(query, filmRowMapper, countFilms);
    }

    @Override
    public void delLike(Integer userId, Integer filmId) {
        if (userId <= 0 || filmId <= 0) {
            String message = String.format(NOT_FOUND_FILM, filmId);
            throw new NotFoundException(message);
        }
        String sql = "DELETE FROM user_likes " +
                "WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);

    }

    private static Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("mpa_id", film.getMpa().getId());
        values.put("rate", film.getRate());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());

        return values;
    }
}