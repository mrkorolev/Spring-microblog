package ru.specialist.spring.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/1.0/post")
public class PostApiController {

    private final PostService postService;

    @Autowired
    public PostApiController(PostService postService) {
        this.postService = postService;
    }


    // Issues: what will actually be in te response?
    // Cyclic dependencies case (for Entity classes): Post -> Comment -> Post -> ...
    // In such situations DTO is perfect - you choose what to give in the JSON

    // It is bad practice to return the body of the response from the method
    // Use ResponseEntity
//    @GetMapping
//    public List<PostDto> findAll() {
//        return postService.findAll().stream()
//                .map(PostDto::new).collect(Collectors.toList());
//    }

    @GetMapping
    public ResponseEntity<List<PostDto>> findAll() {

        return ResponseEntity.ok(postService.findAll()
                        .stream()
                        .map(PostDto::new)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> findById(@PathVariable Long postId) {
        return ResponseEntity.ok(new PostDto(postService.findById(postId)));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.create(postDto));
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long postId,
                       @RequestBody PostDto postDto) {
        postDto.setPostId(postId);
        postService.update(postDto);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
