package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User changeUser(User user);

    void deleteUser(int userId);

    List<User> getAllUsers();
}
