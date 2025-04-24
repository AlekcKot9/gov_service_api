package com.javaproject.myproject;

import gov_service_api.dto.user.*;
import gov_service_api.model.User;
import gov_service_api.repository.*;
import gov_service_api.repository.cache.*;
import gov_service_api.security.*;
import gov_service_api.service.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testSignupUser_WhenUserDoesNotExist() {
        SignupDTO signupDTO = new SignupDTO("12345", "John", "Doe", "+123456789", "address", "password");

        when(userRepository.existsByPersonalId("12345")).thenReturn(false); // Пользователь не существует
        when(userRepository.save(any(User.class))).thenReturn(new User());

        boolean result = userService.signup(signupDTO);

        assertTrue(result, "User should be successfully registered");
        verify(userRepository, times(1)).save(any(User.class)); // Убедимся, что метод save был вызван
    }

    @Test
    void testSignupUser_WhenUserExists() {
        SignupDTO signupDTO = new SignupDTO("12345", "John", "Doe", "+123456789", "address", "password");

        when(userRepository.existsByPersonalId("12345")).thenReturn(true); // Пользователь уже существует

        boolean result = userService.signup(signupDTO);

        assertFalse(result, "User should not be registered because personalId already exists");
        verify(userRepository, never()).save(any(User.class)); // Метод save не должен быть вызван
    }

    @Test
    void testLogin_WhenCredentialsAreCorrect() {
        LoginDTO loginDTO = new LoginDTO("12345", "password");
        User user = new User("12345", "John", "Doe", "+123456789", "address", "hashedPassword");

        when(userRepository.findByPersonalId("12345")).thenReturn(user);
        when(PasswordUtil.checkPassword("password", "hashedPassword")).thenReturn(true);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        boolean result = userService.login(loginDTO, request);

        assertTrue(result, "Login should be successful");
        verify(session, times(1)).setAttribute("personalId", "12345"); // Проверяем, что сессия была установлена
    }

    @Test
    void testLogin_WhenCredentialsAreIncorrect() {
        LoginDTO loginDTO = new LoginDTO("12345", "wrongPassword");
        User user = new User("12345", "John", "Doe", "+123456789", "address", "hashedPassword");

        when(userRepository.findByPersonalId("12345")).thenReturn(user);
        when(PasswordUtil.checkPassword("wrongPassword", "hashedPassword")).thenReturn(false);

        HttpServletRequest request = mock(HttpServletRequest.class);

        boolean result = userService.login(loginDTO, request);

        assertFalse(result, "Login should fail with incorrect password");
    }


}

