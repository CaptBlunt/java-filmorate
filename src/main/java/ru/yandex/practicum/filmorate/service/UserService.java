package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ReAddAsFriends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    public void addFriends(int userId, int friendId) {
        User user = inMemoryUserStorage.findUserById(userId);
        User friend = inMemoryUserStorage.findUserById(friendId);

        Set<Integer> usId = user.getFriends();
        Set<Integer> frId = friend.getFriends();

        if (usId.contains(friendId)) {
            throw new ReAddAsFriends(String.format("Пользователи с id %d и id %d уже являются друзьями",
                    friendId, userId));
        }
        usId.add(friendId);
        user.setFriends(usId);
        frId.add(userId);
        friend.setFriends(frId);
    }

    public List<User> getFriendsById(Integer userId) {
        User user = inMemoryUserStorage.findUserById(userId);
        List<User> users = new ArrayList<>();

        for (Integer id : user.getFriends()) {
            if (inMemoryUserStorage.findUserById(id) != null) {
                users.add(inMemoryUserStorage.findUserById(id));
            }
        }
        return users;
    }

    public List<User> getMutualFriends(int userId, int friendId) {
        User user = inMemoryUserStorage.findUserById(userId);
        User friend = inMemoryUserStorage.findUserById(friendId);
        List<User> users = new ArrayList<>();

        for (Integer id : user.getFriends()) {
            if (friend.getFriends().contains(id)) {
                users.add(inMemoryUserStorage.findUserById(id));
            }
        }
        return users;
    }

    public void delFriend(int userId, int friendId) {
        User user = inMemoryUserStorage.findUserById(userId);
        User friend = inMemoryUserStorage.findUserById(friendId);

        Set<Integer> usId = user.getFriends();
        Set<Integer> frId = friend.getFriends();

        usId.remove(friendId);
        user.setFriends(usId);
        frId.remove(userId);
        friend.setFriends(frId);
    }
}
