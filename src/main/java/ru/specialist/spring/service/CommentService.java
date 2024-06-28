package ru.specialist.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.specialist.spring.dto.CommentDto;
import ru.specialist.spring.entity.Comment;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.repository.CommentRepository;
import ru.specialist.spring.repository.PostRepository;
import ru.specialist.spring.repository.UserRepository;
import ru.specialist.spring.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    @Autowired
    public CommentService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow();
    }

    @Transactional
    public List<Comment> findByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.getComments().size();
        return post.getComments();
    }

    @PreAuthorize("hasRole('REGULAR')")
    public Long create(String content, Long postId) {

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setDtCreated(LocalDateTime.now());
        comment.setPost(postRepository.getById(postId));

        comment.setPost(postRepository.getById(postId));
        comment.setUser(userRepository.findByUsername(SecurityUtils.getCurrentUserDetails().getUsername()).orElseThrow());
        return commentRepository.save(comment).getId();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    public void delete(Long commentId) {
        String currentUser = SecurityUtils.getCurrentUserDetails().getUsername();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (currentUser.equals(comment.getUser().getUsername())) {
            commentRepository.delete(comment);
        }
    }
}
