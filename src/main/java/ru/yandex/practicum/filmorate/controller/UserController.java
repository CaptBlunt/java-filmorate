package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int userId) {
        log.info("Пришёл GET запрос /users/" + userId);
        User user = userService.getUserById(userId);
        log.info("Отправлен ответ getUserById /users/" + userId + "с телом : {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUser() {
        log.info("Пришёл GET запрос /users");
        List<User> users = userService.getAllUsers();
        log.info("Отправлен ответ getAllUser /users с телом {}", users);
        return users;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User newUser) {
        log.info("Пришёл POST запрос /users с телом {}", newUser);
        validationUser(newUser);
        userService.addUser(newUser);
        log.info("Пришёл ответ addUser /users с телом {}", newUser);
        return newUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int userId) {
        log.info("Пришёл DELETE запрос /users/" + userId);
        userService.deleteUser(userId);
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Пришёл PUT запрос /users с телом: {}", user);
        validationUser(user);
        log.info("Отправлен ответ changeFilm /films с телом : {}", user);
        return userService.changeUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Integer userid, @PathVariable("friendId") Integer friendId) {
        log.info("Пришёл PUT запрос /users/" + userid + "/friends/" + friendId);
        userService.addFriends(userid, friendId);
        log.info("Отправлен ответ addFriends /users/" + userid + "/friends/" + friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriendsById(@PathVariable("id") int userId) {
        log.info("Пришёл GET запрос /users/" + userId + "/friends");
        List<User> friends = userService.getAllFriendsById(userId);
        log.info("Отправлен ответ getAllFriendsById /users/" + userId + "/friends с телом {}", friends);
        return friends;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        log.info("Пришёл DEL запрос /users/" + userId + "/friends/" + friendId);
        userService.delFriend(userId, friendId);
        log.info("Отправлен ответ delFriend /users/" + userId + "/friends/" + friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") int userId, @PathVariable("otherId") int friendId) {
        log.info("Пришёл GET запрос /users/" + userId + "/friends/common/" + friendId);
        List<User> mutualFriends = userService.getMutualFriends(userId, friendId);
        log.info("Отправлен ответ getMutualFriends /users/" + userId + "/friends/common/" +
                friendId + " с телом{}", mutualFriends);
        return mutualFriends;
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