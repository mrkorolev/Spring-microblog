package ru.specialist.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.User;
import ru.specialist.spring.repository.PostRepository;
import ru.specialist.spring.repository.UserRepository;
import ru.specialist.spring.service.PostService;
import ru.specialist.spring.service.UserService;
import ru.specialist.spring.util.SecurityUtils;

import javax.servlet.ServletContext;
import java.util.List;

@Controller
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostService postService;
    private final ServletContext servletContext;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository,
                          UserService userService,
                          PostService postService, ServletContext servletContext) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.postService = postService;
        this.servletContext = servletContext;
    }

    @GetMapping
    public String posts(@RequestParam(name = "q", required = false) String query, ModelMap model) {
        List<Post> posts = StringUtils.hasText(query) ?
                postRepository.findByContentContainingIgnoreCaseOrderByDtCreatedDesc(query) :
                postRepository.findAll(Sort.by("dtCreated").descending());
        model.put("posts", posts);
        model.put("title", StringUtils.hasText(query) ? "Search by:" : "All posts");
        model.put("subtitle", StringUtils.hasText(query) ?
                ("\"" + (query.length() > 15 ? query.substring(0, 15) + "..." : query) + "\"") : "");
        setCommonParams(model);
        return "blog";
    }

    @GetMapping("/post/{postId}")
    @PreAuthorize("hasRole('REGULAR')")
    public String post(@PathVariable Long postId, ModelMap model) {
        model.put("post", postService.findById(postId));
        setCommonParams(model);
        return "post";
    }

    @GetMapping("/post/new")
    @PreAuthorize("hasRole('REGULAR')")
    public String postNew(ModelMap model) {
        return "post-new";
    }

    @PostMapping("/post/new")
    @PreAuthorize("hasRole('REGULAR')")
    public String postNew(PostDto postDto) {     // pass as a single dto object
        long postId = postService.create(postDto);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/user/{username}")
    public String postsByUser(@PathVariable String username,
                              ModelMap model) {
        User user = userService.findByUsername(username);
        model.put("posts", user.getPosts());
        model.put("title", "Posts for:");
        model.put("subtitle", username);
        setCommonParams(model);

        return "blog";
    }

    @GetMapping("/post/{postId}/edit")
    @PreAuthorize("hasRole('REGULAR')")
    public String postEdit(@PathVariable Long postId, ModelMap model) {
        Post postToUpdate = postService.findById(postId);
        SecurityUtils.checkAuthorityOnPost(postToUpdate);

        model.put("post", new PostDto(postToUpdate));
        setCommonParams(model);
        return "post-edit";
    }

    @PostMapping("/post/edit")
    @PreAuthorize("hasRole('REGULAR')")
    public String postEdit(PostDto postDto) {
        postService.update(postDto);
        return "redirect:/post/" + postDto.getPostId();
    }

    @PostMapping("/post/{postId}/delete")     // no page update, this is called from JS without reload
    @ResponseStatus(HttpStatus.OK)
    public void postDelete(@PathVariable Long postId) {
        postService.delete(postId);
    }

    private void setCommonParams(ModelMap model) {
        model.put("users", userRepository.findAll());
        model.put("contextPath", servletContext.getContextPath());
    }
}
