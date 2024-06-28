package ru.specialist.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.Tag;
import ru.specialist.spring.entity.User;
import ru.specialist.spring.repository.PostRepository;
import ru.specialist.spring.repository.TagRepository;
import ru.specialist.spring.repository.UserRepository;
import ru.specialist.spring.util.SecurityUtils;

import static ru.specialist.spring.util.SecurityUtils.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(TagRepository tagRepository, UserRepository userRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        List<Post> posts = postRepository.findAll();
        posts.forEach(p -> p.getTags().size());
        return posts;
    }

    public Post findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.getTags().size();
        post.getComments().size();
        return post;
    }


    @PreAuthorize("hasRole('REGULAR')")
    public Long create(PostDto dto) {
        Post newPost = new Post();
        newPost.setTitle(dto.getTitle());
        newPost.setContent(dto.getContent());
        newPost.setTags(parseTags(dto.getTags()));

        User currentUser = userRepository.findByUsername(getCurrentUserDetails().getUsername()).orElseThrow();
        newPost.setUser(currentUser);
        newPost.setDtCreated(LocalDateTime.now());
        return postRepository.save(newPost).getId();
    }

    @PreAuthorize("hasRole('REGULAR')")
    public void update(PostDto postDto) {
        Post postToUpdate = postRepository.findById(postDto.getPostId()).orElseThrow();
        SecurityUtils.checkAuthorityOnPost(postToUpdate);

        if (postDto.getTitle() != null) {
            postToUpdate.setTitle(StringUtils.hasText(postDto.getTitle())
                    ? postDto.getTitle()
                    : "");
        }

        if (postDto.getContent() != null) {
            postToUpdate.setContent(StringUtils.hasText(postDto.getContent())
                    ? postDto.getContent()
                    : "");
        }

        if (postDto.getTags() != null) {
            postToUpdate.setTags(StringUtils.hasText(postDto.getTags())
                    ? parseTags(postDto.getTags())
                    : Collections.emptyList());
        }

        postToUpdate.setDtUpdated(LocalDateTime.now());
        postRepository.save(postToUpdate);
    }

    @PreAuthorize("hasAnyRole('REGULAR', 'ADMIN')")
    public void delete(Long postId) {
        checkAuthorityOnPostOrUserAdmin(postRepository.findById(postId).orElseThrow());
        postRepository.deleteById(postId);
    }

    private List<Tag> parseTags(String tags) {
        if (tags == null)  {
            return Collections.emptyList();
        }

        return Arrays.stream(tags.split(" "))
                .map(tag -> tagRepository.findByName(tag).orElseGet(() -> tagRepository.save(new Tag(tag))))
                .collect(Collectors.toList());
    }
}
