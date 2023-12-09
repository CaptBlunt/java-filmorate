package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    UserController userController;

    @BeforeEach
    public void setUp() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        userController = new UserController(new UserService(inMemoryUserStorage), inMemoryUserStorage);
    }

    @Test
    public void addUserTest() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        Set<Integer> friends = new TreeSet<>();
        User user = new User(1, "fae@ya.ru", "login", "Name", birthDay, friends);

        userController.addUser(user);

        List<User> users = userController.getAllUsers();

        assertEquals(1, users.size(), "Пользователь не добавился");
        assertEquals(user, users.get(0), "Данные не совпадают");
    }

    @Test
    public void emptyEmail() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        Set<Integer> friends = new TreeSet<>();
        User user = new User(1, "", "login", "Name", birthDay, friends);

        assertThrows(ValidationException.class, () -> userController.addUser(user), "Валидация работает не правильно");
    }

    @Test
    public void emailDoesNotContainCommercialAt() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        Set<Integer> friends = new TreeSet<>();
        User user = new User(1, "faeya.ru", "login", "Name", birthDay, friends);

        assertThrows(ValidationException.class, () -> userController.addUser(user), "Валидация работает не правильно");
    }

    @Test
    public void emptyLogin() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        Set<Integer> friends = new TreeSet<>();
        User user = new User(1, "faeya@.ru", "", "Name", birthDay, friends);

        assertThrows(ValidationException.class, () -> userController.addUser(user), "Валидация работает не правильно");
    }

    @Test
    public void loginContainsSpace() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        Set<Integer> friends = new TreeSet<>();
        User user = new User(1, "faeya@.ru", "ja va", "Name", birthDay, friends);

        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void birthDayTomorrow() {
        LocalDate birthDay = LocalDate.now().plusDays(1L);
        Set<Integer> friends = new TreeSet<>();
        User user = new User(1, "faeya@.ru", "java", "Name", birthDay, friends);

        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void birthDayToday() {
        LocalDate birthDay = LocalDate.now();
        Set<Integer> friends = new TreeSet<>();
        User user = new User(1, "faeya@.ru", "java", "Name", birthDay, friends);

        userController.addUser(user);

        List<User> users = userController.getAllUsers();

        assertEquals(1, users.size(), "Пользователь не добавился");
        assertEquals(user, users.get(0), "Данные не совпадают");
    }

    @Test
    public void birthDayYesterday() {
        LocalDate birthDay = LocalDate.now().minusDays(1L);
        Set<Integer> friends = new TreeSet<>();
        User user = new User(1, "faeya@.ru", "java", "Name", birthDay, friends);

        userController.addUser(user);

        List<User> users = userController.getAllUsers();

        assertEquals(1, users.size(), "Пользователь не добавился");
        assertEquals(user, users.get(0), "Данные не совпадают");
    }
}
