package ru.job4j.dreamjob.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

class UserControllerTest {

    private UserController userController;

    private UserService userService;

    @BeforeEach
    void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void whenSuccessfullyRegistered() {
        var user = new User(1, "email", "name", "password");
        when(userService.save(user)).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(user, model);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    void whenRegisterUnsuccessfully() {
        var user = new User(1, "email", "name", "password");
        when(userService.save(user)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(user, model);
        var exception = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(exception).isEqualTo("Пользователь с такой почтой уже существует");
    }

    @Test
    void whenLoginIsSuccessful() {
        var user = new User(1, "email", "name", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));

        var request = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("redirect:/vacancies");
        verify(session).setAttribute("user", user);
    }

    @Test
    void whenLoginIsUnsuccessful() {
        var user = new User(1, "email", "name", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());

        var request = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("users/login");
        assertThat(model.getAttribute("error")).isEqualTo("Почта или пароль введены неверно");
    }
}