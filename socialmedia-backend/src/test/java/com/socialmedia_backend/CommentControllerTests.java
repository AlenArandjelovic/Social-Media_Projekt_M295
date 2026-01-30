package com.socialmedia_backend;

import com.socialmedia_backend.controller.CommentController;
import com.socialmedia_backend.model.Comment;
import com.socialmedia_backend.model.Post;
import com.socialmedia_backend.model.User;
import com.socialmedia_backend.service.CommentService;
import com.socialmedia_backend.service.PostService;
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

class CommentControllerTests {

    @Mock
    private CommentService commentService;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentController commentController;

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
        post.setTitle("Test Post");
        post.setUser(user);

        comment1 = new Comment();
        comment1.setContent("Nice post!");
        comment1.setPost(post);
    }

    @Test
    void testGetAllComments() {
        when(commentService.getAllComments()).thenReturn(Arrays.asList(comment1));

        ResponseEntity<List<Comment>> response = commentController.getAllComments();
        List<Comment> comments = response.getBody();

        assertNotNull(comments);
        assertEquals(1, comments.size());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    void testGetCommentById_Found() {
        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment1));

        ResponseEntity<Comment> response = commentController.getCommentById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Nice post!", response.getBody().getContent());
    }

    @Test
    void testGetCommentById_NotFound() {
        when(commentService.getCommentById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Comment> response = commentController.getCommentById(2L);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testCreateComment() {
        when(postService.getPostById(1L)).thenReturn(Optional.of(post));
        when(commentService.createComment(any(Comment.class))).thenReturn(comment1);

        ResponseEntity<Comment> response = commentController.createComment(1L, comment1);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Nice post!", response.getBody().getContent());
    }

    @Test
    void testDeleteComment() {
        doNothing().when(commentService).deleteComment(1L);

        ResponseEntity<Void> response = commentController.deleteComment(1L);
        assertEquals(204, response.getStatusCodeValue());
        verify(commentService, times(1)).deleteComment(1L);
    }
}
