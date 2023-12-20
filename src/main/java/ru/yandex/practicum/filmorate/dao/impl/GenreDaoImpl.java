package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenreDaoImpl implements GenreDao {
    private static final String NOT_FOUND_MPA = "Жанр с id %d не найден";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * " +
                "FROM genres";
        return jdbcTemplate.query(sqlQuery, getGenreMapper());
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * " +
                "FROM genres " +
                "WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, getGenreMapper(), id);
        } catch (RuntimeException e) {
            String message = String.format(NOT_FOUND_MPA, id);
            log.error(message);
            throw new NotFoundException(message);
        }
    }
    private static RowMapper<Genre> getGenreMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("id"),
                rs.getString("name"));
    }
}
