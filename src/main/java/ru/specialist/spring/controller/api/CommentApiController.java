package ru.specialist.spring.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.specialist.spring.dto.CommentDto;
import ru.specialist.spring.entity.Comment;
import ru.specialist.spring.service.CommentService;
import ru.specialist.spring.util.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/1.0/comment")
public class CommentApiController {

    private final CommentService commentService;

    @Autowired
    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> findAll() {
        return ResponseEntity.ok(
                commentService.findAll().stream()
                        .map(CommentDto::new)
                        .collect(Collectors.toList())

        );
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> findById(@PathVariable Long commentId) {
        return ResponseEntity.ok(new CommentDto(commentService.findById(commentId)));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> findAllForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(
            commentService.findByPostId(postId)
                    .stream()
                    .map(CommentDto::new)
                    .collect(Collectors.toList())
        );
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<Long> create(@PathVariable Long postId, @RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.create(dto.getContent(), postId));
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }
}
