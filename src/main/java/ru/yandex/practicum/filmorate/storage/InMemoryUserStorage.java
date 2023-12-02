package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;
    private final String NOT_FOUND_USER = "Пользователь с id %d не найден";

    @Override
    public User addUser(User user) {
        user.setId(++generatorId);
        user.setFriends(new TreeSet<>());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User changeUser(User userUpd) {
        if (users.containsKey(userUpd.getId())) {
            User mutableUser = users.get(userUpd.getId());
            Set<Integer> userIds = mutableUser.getFriends();
            userUpd.setFriends(userIds);
            users.put(userUpd.getId(), userUpd);
        } else {
            String message = String.format(NOT_FOUND_USER, userUpd.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
        return userUpd;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(int userId) {
        if (!users.containsKey(userId)) {
            String message = String.format(NOT_FOUND_USER, userId);
            log.error(message);
            throw new NotFoundException(message);
        }
        users.remove(userId);
    }

    public User findUserById(Integer userId) {
        return getAllUsers().stream().filter(p -> p.getId() == userId).findFirst().orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_USER, userId)));
    }
}
