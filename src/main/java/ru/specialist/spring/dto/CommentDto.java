package ru.specialist.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.specialist.spring.entity.Comment;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private Long commentId;
    private Long postId;
    private String content;
    private String author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtUpdated;

    public CommentDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.author = comment.getUser().getUsername();
        this.dtCreated = comment.getDtCreated();
        this.dtUpdated = comment.getDtUpdated();
    }

    public CommentDto(String content) {
        this.content = content;
    }
}
