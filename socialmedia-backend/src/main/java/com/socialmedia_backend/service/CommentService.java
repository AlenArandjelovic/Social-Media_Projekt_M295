package com.socialmedia_backend.service;

import com.socialmedia_backend.model.Comment;
import com.socialmedia_backend.model.Post;
import com.socialmedia_backend.model.User;
import com.socialmedia_backend.repository.CommentRepository;
import com.socialmedia_backend.repository.PostRepository;
import com.socialmedia_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // kommentare erstellen
    public Comment createComment(Long postId, Long userId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        comment.setPost(post);
        comment.setUser(user);

        return commentRepository.save(comment);
    }

    // alle Kommentare abrufen
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // Kommentar nach Id abrufen
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    // Kommentare nach PostId abrufen
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return commentRepository.findByPostId(post.getId());
    }

    // Kommentar aktualisieren
    public Comment updateComment(Long id, Comment updatedComment) {
        return commentRepository.findById(id)
                .map(comment -> {
                    comment.setContent(updatedComment.getContent());
                    return commentRepository.save(comment);
                })
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    // Kommentar löschen
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.deleteById(id);
    }

    // in CommentService
    public Comment createComment(Comment comment) {
    return commentRepository.save(comment);
}

}
