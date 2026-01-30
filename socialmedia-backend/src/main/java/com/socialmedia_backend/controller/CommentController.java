package com.socialmedia_backend.controller;

import com.socialmedia_backend.model.Comment;
import com.socialmedia_backend.model.Post;
import com.socialmedia_backend.model.User;
import com.socialmedia_backend.service.CommentService;
import com.socialmedia_backend.service.PostService;
import com.socialmedia_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    // erstelle einen kommentar für einen post
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody Comment comment) {

        try {
            // post prüfen
            Post post = postService.getPostById(postId)
                    .orElseThrow(() -> new RuntimeException("Post nicht gefunden"));

            // user prüfen
            if (comment.getUser() == null || comment.getUser().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User fehlt oder userId ist null");
            }

            User user = userService.getUserById(comment.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

            // post und user zuweisen
            comment.setPost(post);
            comment.setUser(user);

            Comment savedComment = commentService.createComment(comment);
            return ResponseEntity.ok(savedComment);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // alle kommentare für einen post abrufen
    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    // einzelner kommentar abrufen
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // kommentar aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @Valid @RequestBody Comment comment) {
        try {
            if (comment.getUser() == null || comment.getUser().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User fehlt oder userId ist null");
            }

            User user = userService.getUserById(comment.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

            comment.setUser(user);
            Comment updatedComment = commentService.updateComment(id, comment);
            return ResponseEntity.ok(updatedComment);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // kommentar löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
