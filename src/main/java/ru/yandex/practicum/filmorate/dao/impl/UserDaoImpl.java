package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDaoImpl implements UserDao {
    private static final String NOT_FOUND_USER = "Пользователь с id %d не найден";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * " +
                "FROM users";
        return jdbcTemplate.query(sqlQuery, getUserMapper());
    }

    @Override
    public User getUserById(int userId) {
        String sqlQuery = "SELECT * " +
                "FROM users " +
                "WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, getUserMapper(), userId);
        } catch (RuntimeException e) {
            String message = String.format(NOT_FOUND_USER, userId);
            log.error(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        int id = insert.executeAndReturnKey(userToMap(user)).intValue();
        return user.setId(id);
    }

    @Override
    public void deleteUser(int userId) {
        String sqlQuery = "DELETE FROM users " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public void addFriends(Integer userId, Integer friendId) {
        getUserById(userId);
        getUserById(friendId);
        String sqlQuery = "INSERT INTO friends(user_id, friend_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public User changeUser(User userUpd) {
        getUserById(userUpd.getId());
        String sqlQuery = "UPDATE users " +
                "SET email = ?, login = ?, name_user = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, userUpd.getEmail(), userUpd.getLogin(),
                userUpd.getName(), userUpd.getBirthday(), userUpd.getId());
        return getUserById(userUpd.getId());

    }

    @Override
    public List<User> getAllFriendsById(Integer userId) {
        String sqlQuery = "SELECT * " +
                "FROM users u " +
                "JOIN friends f ON u.id = f.friend_id " +
                "WHERE f.user_id = ?";
        return jdbcTemplate.query(sqlQuery, getUserMapper(), userId);
    }

    @Override
    public void delFriend(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM friends " +
                "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        String sqlQuery = "SELECT * " +
                "FROM users " +
                "WHERE id IN (SELECT friend_id " +
                "FROM friends " +
                "WHERE user_id = ? AND friend_id IN (SELECT friend_id " +
                "FROM friends " +
                "WHERE user_id = ?));";
        return jdbcTemplate.query(sqlQuery, getUserMapper(), userId, friendId);
    }

    private static RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> new User(rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name_user"),
                rs.getDate("birthday").toLocalDate());
    }

    private static Map<String, Object> userToMap(User user) {
        return Map.of("email", user.getEmail(),
                "login", user.getLogin(),
                "name_user", user.getName(),
                "birthday", user.getBirthday());
    }
}
