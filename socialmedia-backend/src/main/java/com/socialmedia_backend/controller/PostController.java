package com.socialmedia_backend.controller;

import com.socialmedia_backend.model.Post;
import com.socialmedia_backend.model.User;
import com.socialmedia_backend.service.PostService;
import com.socialmedia_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // Create Post
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody Post post) {
        try {
            // Prüfen, ob User im JSON existiert
            if (post.getUser() == null || post.getUser().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("User fehlt oder userId ist null");
            }

            // User aus DB holen
            User user = userService.getUserById(post.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

            // Post User zuweisen
            post.setUser(user);

            // Post speichern
            Post savedPost = postService.createPost(post);
            return ResponseEntity.ok(savedPost);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // Get all posts
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // Get post by id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update post
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @Valid @RequestBody Post post) {
        try {
            if (post.getUser() == null || post.getUser().getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("User fehlt oder userId ist null");
            }

            User user = userService.getUserById(post.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

            post.setUser(user);

            Post updatedPost = postService.updatePost(id, post);
            return ResponseEntity.ok(updatedPost);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // Delete post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
