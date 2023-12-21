package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User changeUser(User user);

    void deleteUser(int userId);

    List<User> getAllUsers();

    User getUserById(int id);

    void addFriends(Integer userId, Integer friendId);

    List<User> getAllFriendsById(int userId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getMutualFriends(int userId, int friendId);
}

