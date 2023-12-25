package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenresStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmGenresService implements FilmGenresStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres(int filmId) {
        String sql = "SELECT g.id, g.name " +
                " FROM genres g " +
                " JOIN film_genres fg ON g.id = fg.genre_id " +
                " WHERE fg.film_id = ?";

        return jdbcTemplate.query(sql, this::mapRowToGenre, filmId);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int i) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private static RowMapper<Genre> getGenreMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("id"),
                rs.getString("name"));
    }
}

