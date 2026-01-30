package com.socialmedia_backend;

import com.socialmedia_backend.model.User;
import com.socialmedia_backend.repository.UserRepository;
import com.socialmedia_backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser");
        user1.setEmail("test@example.com");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1));

        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User created = userService.createUser(user1);
        assertNotNull(created);
        assertEquals("testuser", created.getUsername());
    }

@Test
void testDeleteUser() {
    // Step 1: Mock für existsById setzen
    when(userRepository.existsById(1L)).thenReturn(true);

    // Step 2: Mock für deleteById setzen (optional)
    doNothing().when(userRepository).deleteById(1L);

    // Step 3: Delete aufrufen
    userService.deleteUser(1L);

    // Step 4: Verify dass deleteById aufgerufen wurde
    verify(userRepository, times(1)).deleteById(1L);
}


}
