package ru.specialist.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.Role;
import ru.specialist.spring.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long userId;
    private String username;
    private String password;
    private List<Long> postIds;
    private String roles;
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtCreated;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dtUpdated;

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDto(Boolean isActive, String username) {
        this.isActive = isActive;
        this.username = username;
    }

    public UserDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.postIds = user.getPosts().stream()
                .map(Post::getId)
                .collect(Collectors.toList());
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));
        this.isActive = user.getIsActive();
        this.dtCreated = user.getDtCreated();
        this.dtUpdated = user.getDtUpdated();
    }
}
