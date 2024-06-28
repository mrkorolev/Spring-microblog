package ru.specialist.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.Tag;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

    private Long postId;
    private String title;
    private String content;
    private String tags;
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")    // JSR310 output is not good enough
    private LocalDateTime dtCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtUpdated;

    public PostDto(String title) {
        this.title = title;
    }

    public PostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostDto(Post post) {

        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tags = post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(" "));

        // Jackson doesn't support Java8 LocalDateTime - datatype jsr310 is needed
        this.dtCreated = post.getDtCreated();
        this.dtUpdated = post.getDtUpdated();
        this.username = post.getUser().getUsername();
    }
}
