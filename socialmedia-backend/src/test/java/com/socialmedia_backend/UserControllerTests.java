package com.socialmedia_backend;

import com.socialmedia_backend.controller.UserController;
import com.socialmedia_backend.model.User;
import com.socialmedia_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // User-Objekt vorbereiten
        user1 = new User();
        // User ID setter hinzufügen oder Konstruktor anpassen
        user1.setUsername("testuser");
        user1.setEmail("test@example.com");
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1));

        ResponseEntity<List<User>> response = userController.getAllUsers();
        List<User> users = response.getBody();

        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_Found() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user1));

        ResponseEntity<User> response = userController.getUserById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(2L);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(any(User.class))).thenReturn(user1);

        ResponseEntity<User> response = userController.createUser(user1);
        assertEquals(200, response.getStatusCodeValue()); // Controller gibt aktuell 200, nicht 201
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }
}
