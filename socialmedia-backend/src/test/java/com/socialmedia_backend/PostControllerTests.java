package com.socialmedia_backend;

import com.socialmedia_backend.controller.PostController;
import com.socialmedia_backend.model.Post;
import com.socialmedia_backend.model.User;
import com.socialmedia_backend.service.PostService;
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

class PostControllerTests {

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostController postController;

    private Post post1;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        post1 = new Post();
        post1.setTitle("Test Post");
        post1.setContent("Content");
        post1.setUser(user);
    }

    @Test
    void testGetAllPosts() {
        when(postService.getAllPosts()).thenReturn(Arrays.asList(post1));

        ResponseEntity<List<Post>> response = postController.getAllPosts();
        List<Post> posts = response.getBody();

        assertNotNull(posts);
        assertEquals(1, posts.size());
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void testGetPostById_Found() {
        when(postService.getPostById(1L)).thenReturn(Optional.of(post1));

        ResponseEntity<Post> response = postController.getPostById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Post", response.getBody().getTitle());
    }

    @Test
    void testGetPostById_NotFound() {
        when(postService.getPostById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Post> response = postController.getPostById(2L);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testCreatePost() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(postService.createPost(any(Post.class))).thenReturn(post1);

        ResponseEntity<Post> response = postController.createPost(post1);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Post", response.getBody().getTitle());
    }

    @Test
    void testDeletePost() {
        doNothing().when(postService).deletePost(1L);

        ResponseEntity<Void> response = postController.deletePost(1L);
        assertEquals(204, response.getStatusCodeValue());
        verify(postService, times(1)).deletePost(1L);
    }
}
