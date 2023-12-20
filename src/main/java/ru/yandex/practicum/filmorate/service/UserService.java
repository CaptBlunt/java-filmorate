package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserStorage {
    private final UserDao userDao;

    @Override
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void deleteUser(int userId) {
        userDao.deleteUser(userId);
    }

    @Override
    public User changeUser(User user) {
        return userDao.changeUser(user);
    }

    @Override
    public void addFriends(Integer userId, Integer friendId) {
        userDao.addFriends(userId, friendId);
    }

    @Override
    public List<User> getAllFriendsById(int userId) {
        return userDao.getAllFriendsById(userId);
    }

    @Override
    public void delFriend(Integer userId, Integer friendId) {
        userDao.delFriend(userId, friendId);
    }

    @Override
    public List<User> getMutualFriends(int userId, int friendId) {
        return userDao.getMutualFriends(userId, friendId);
    }
}