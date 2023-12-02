package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(UserService userService, InMemoryUserStorage inMemoryUserStorage) {
        this.userService = userService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Пришел Post запрос /users с телом: {}", user);
        validationUser(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        inMemoryUserStorage.addUser(user);
        log.info("Отправлен ответ addUser /users с телом : {}", user);
        return user;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Пришёл PUT запрос /users с телом: {}", user);
        validationUser(user);
        inMemoryUserStorage.changeUser(user);
        log.info("Отправлен ответ changeUser /users с телом : {}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Integer userid, @PathVariable("friendId") Integer friendId) {
        log.info("Пришёл PUT запрос /users/" + userid + "/friends/" + friendId);
        userService.addFriends(userid, friendId);
        log.info("Отправлен ответ addFriends /users/" + userid + "/friends/" + friendId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Пришёл GET запрос /users");
        List<User> users = inMemoryUserStorage.getAllUsers();
        log.info("Отправлен ответ getAllUsers /users с телом : {}", users);
        return users;
    }


    @GetMapping("/{id}/friends")
    public List<User> getFriendsById(@PathVariable("id") Integer userid) {
        log.info("Пришёл GET запрос /users/" + userid + "/friends");
        List<User> users = userService.getFriendsById(userid);
        log.info("Отправлен ответ getFriendsById /users/" + userid + "/friends с телом : {}", users);
        return users;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") int userId, @PathVariable("otherId") int friendId) {
        log.info("Пришёл GET запрос /users/" + userId + "/friends/common/" + friendId);
        List<User> mutualFriends = userService.getMutualFriends(userId, friendId);
        log.info("Отправлен ответ getMutualFriends /users/" + userId + "/friends/common/" + friendId + "с телом : {}",
                mutualFriends);
        return mutualFriends;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        log.info("Пришёл GET запрос /users/" + userId);
        User user = inMemoryUserStorage.findUserById(userId);
        log.info("Отправлен ответ getUserById /users/" + userId + "с телом : {}", user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer userId) {
        log.info("Пришёл DEL запрос /users/" + userId);
        inMemoryUserStorage.deleteUser(userId);
        log.info("Отправлен ответ deleteUser /users/" + userId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        log.info("Пришёл DEL запрос /users/" + userId + "/friends/" + friendId);
        userService.delFriend(userId, friendId);
        log.info("Отправлен ответ delFriend /users/" + userId + "/friends/" + friendId);
    }

    public void validationUser(User user) {
        String userEmail = user.getEmail();
        String userLogin = user.getLogin();
        String userName = user.getName();
        String message;
        LocalDate userBirthday = user.getBirthday();
        if (userEmail == null || userEmail.isEmpty() || !(userEmail.contains("@"))) {
            message = "Электронная почта не может быть пустой и должна содержать символ @";
            log.error(message);
            throw new ValidationException(message);
        } else if (userLogin.isEmpty() || userLogin.contains(" ")) {
            message = "Логин не может быть пустым и содержать пробелы";
            log.error(message);
            throw new ValidationException(message);
        } else if (userBirthday.isAfter(LocalDate.now())) {
            message = "Дата рождения не может быть в будущем";
            log.error(message);
            throw new ValidationException(message);
        } else if (userName == null || userName.isEmpty()) {
            user.setName(userLogin);
        }
    }
}
