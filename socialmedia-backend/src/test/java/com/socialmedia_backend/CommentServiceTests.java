package com.socialmedia_backend;

import com.socialmedia_backend.model.Comment;
import com.socialmedia_backend.model.Post;
import com.socialmedia_backend.model.User;
import com.socialmedia_backend.repository.CommentRepository;
import com.socialmedia_backend.service.CommentService;
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

class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment1;
    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        post = new Post();
        post.setId(1L);
        post.setUser(user);
        post.setTitle("Test Post");

        comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("Nice post!");
        comment1.setPost(post);
    }

    @Test
    void testGetAllComments() {
        when(commentRepository.findAll()).thenReturn(Arrays.asList(comment1));

        List<Comment> comments = commentService.getAllComments();
        assertEquals(1, comments.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testGetCommentById_Found() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));

        Optional<Comment> result = commentService.getCommentById(1L);
        assertTrue(result.isPresent());
        assertEquals("Nice post!", result.get().getContent());
    }

    @Test
    void testGetCommentById_NotFound() {
        when(commentRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Comment> result = commentService.getCommentById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateComment() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment1);

        Comment created = commentService.createComment(comment1);
        assertNotNull(created);
        assertEquals("Nice post!", created.getContent());
    }

    @Test
    void testDeleteComment() {
        when(commentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).deleteById(1L);
    }
}
