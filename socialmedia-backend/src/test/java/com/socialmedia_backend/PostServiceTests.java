package com.socialmedia_backend;

import com.socialmedia_backend.model.Post;
import com.socialmedia_backend.model.User;
import com.socialmedia_backend.repository.PostRepository;
import com.socialmedia_backend.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTests {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post post1;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Test Post");
        post1.setContent("Content");
        post1.setUser(user);
    }

    @Test
    void testGetAllPosts() {
        when(postRepository.findAll()).thenReturn(Arrays.asList(post1));

        List<Post> posts = postService.getAllPosts();
        assertEquals(1, posts.size());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void testGetPostById_Found() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));

        Optional<Post> result = postService.getPostById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test Post", result.get().getTitle());
    }

    @Test
    void testGetPostById_NotFound() {
        when(postRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Post> result = postService.getPostById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePost() {
        when(postRepository.save(any(Post.class))).thenReturn(post1);

        Post created = postService.createPost(post1);
        assertNotNull(created);
        assertEquals("Test Post", created.getTitle());
    }

    @Test
    void testDeletePost() {
        when(postRepository.existsById(1L)).thenReturn(true);
        doNothing().when(postRepository).deleteById(1L);

        postService.deletePost(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }
}
