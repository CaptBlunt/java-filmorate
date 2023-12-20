package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {

    User getUserById(int userId);

    List<User> getAllUsers();

    User changeUser(User user);

    void deleteUser(int userId);

    void addFriends(Integer userId, Integer friendId);

    List<User> getAllFriendsById(Integer userId);

    void delFriend(Integer userId, Integer friendId);

    List<User> getMutualFriends(Integer userId, Integer friendId);

    User addUser(User user);
}
