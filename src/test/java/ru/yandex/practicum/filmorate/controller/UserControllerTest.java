package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    UserController userController;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void addUserTest() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        User user = new User(1, "fae@ya.ru", "login", "Name", birthDay);

        userController.addUser(user);

        List<User> users = userController.getAllUsers();

        assertEquals(1, users.size(), "Пользователь не добавился");
        assertEquals(user, users.get(0), "Данные не совпадают");
    }

    @Test
    public void emptyEmail() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        User user = new User(1, "", "login", "Name", birthDay);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Валидация работает не правильно");
    }

    @Test
    public void emailDoesNotContainCommercialAt() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        User user = new User(1, "faeya.ru", "login", "Name", birthDay);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Валидация работает не правильно");
    }

    @Test
    public void emptyLogin() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        User user = new User(1, "faeya@.ru", "", "Name", birthDay);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Валидация работает не правильно");
    }

    @Test
    public void loginContainsSpace() {
        LocalDate birthDay = LocalDate.parse("2000-01-01");
        User user = new User(1, "faeya@.ru", "ja va", "Name", birthDay);

        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void birthDayTomorrow() {
        LocalDate birthDay = LocalDate.now().plusDays(1L);
        User user = new User(1, "faeya@.ru", "java", "Name", birthDay);

        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void birthDayToday() {
        LocalDate birthDay = LocalDate.now();
        User user = new User(1, "faeya@.ru", "java", "Name", birthDay);

        userController.addUser(user);

        List<User> users = userController.getAllUsers();

        assertEquals(1, users.size(), "Пользователь не добавился");
        assertEquals(user, users.get(0), "Данные не совпадают");
    }

    @Test
    public void birthDayYesterday() {
        LocalDate birthDay = LocalDate.now().minusDays(1L);
        User user = new User(1, "faeya@.ru", "java", "Name", birthDay);

        userController.addUser(user);

        List<User> users = userController.getAllUsers();

        assertEquals(1, users.size(), "Пользователь не добавился");
        assertEquals(user, users.get(0), "Данные не совпадают");
    }
}
