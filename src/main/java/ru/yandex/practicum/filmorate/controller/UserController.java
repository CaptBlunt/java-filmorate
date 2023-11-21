package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Пришел Post запрос /users с телом: {}", user);
        validationUser(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(++generatorId);
        users.put(user.getId(), user);
        log.info("Отправлен ответ add /users с телом : {}", user);
        return user;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Пришёл PUT запрос /users с телом: {}", user);
        if (!users.containsKey(user.getId())) {
            String message = "Такой пользователь не зарегистрирован";
            log.error(message);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
        validationUser(user);
        users.put(user.getId(), user);
        log.info("Отправлен ответ change /users с телом : {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("GET /users");
        return new ArrayList<>(users.values());
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
