package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class MpaDaoImpl implements MpaDao {

    private static final String NOT_FOUND_MPA = "Рейтинг с id %d не найден";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, getMpaMapper());
    }

    @Override
    public Mpa getMpaById(int id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, getMpaMapper(), id);
        } catch (RuntimeException e) {
            String message = String.format(NOT_FOUND_MPA, id);
            log.error(message);
            throw new NotFoundException(message);
        }
    }

    private static RowMapper<Mpa> getMpaMapper() {
        return (rs, rowNum) -> Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
