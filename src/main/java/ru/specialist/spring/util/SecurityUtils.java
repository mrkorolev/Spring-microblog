package ru.specialist.spring.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.specialist.spring.entity.Post;
import ru.specialist.spring.entity.Role;
import ru.specialist.spring.entity.User;

import java.util.stream.Collectors;

public class SecurityUtils {

    public static final String ACCESS_DENIED = "Access Denied";

    public static UserDetails getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }

        return (UserDetails)principal;
    }

    public static void checkAuthorityOnPost(Post post) {
        if (!hasAuthorityOnPost(post)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }

    public static void checkAuthorityOnPostOrUserAdmin(Post post) {
        if (!hasAuthorityOnPost(post) && !hasRole(Role.ADMIN)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }

    public static boolean hasAuthorityOnPost(Post post) {
        String username = getCurrentUserDetails().getUsername();
        return post.getUser().getUsername().equals(username);
    }

    public static boolean hasRole(String roleName) {
        return getCurrentUserDetails().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()).contains(User.ROLE + roleName);
    }


}
