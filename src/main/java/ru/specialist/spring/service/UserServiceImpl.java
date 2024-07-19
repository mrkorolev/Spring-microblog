package ru.specialist.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.specialist.spring.dto.UserDto;
import ru.specialist.spring.entity.Role;
import ru.specialist.spring.entity.User;
import ru.specialist.spring.repository.RoleRepository;
import ru.specialist.spring.repository.UserRepository;
import ru.specialist.spring.util.SecurityUtils;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static ru.specialist.spring.util.SecurityUtils.ACCESS_DENIED;
import static ru.specialist.spring.util.SecurityUtils.hasRole;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('REGULAR')")
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
        user.getPosts().size();
        user.getRoles().size();
        return user;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> {
            u.getPosts().size();
            u.getAuthorities().size();
        });
        return users;
    }

    @Override
    public Long create(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new EntityExistsException();
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(List.of(roleRepository.findByName(Role.REGULAR).orElseThrow()));
        user.setDtCreated(LocalDateTime.now());
        user.setIsActive(true);
        return userRepository.save(user).getId();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    public User update(UserDto userDto) {
        User user = userRepository.findById(userDto.getUserId()).orElseThrow();
       if (!user.getUsername().equals(SecurityUtils.getCurrentUserDetails().getUsername()) && !hasRole(Role.ADMIN)) {
           throw new AccessDeniedException(ACCESS_DENIED);
       }

        if (userDto.getUsername() != null && StringUtils.hasText(userDto.getUsername())) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getPassword() != null && StringUtils.hasText(userDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if (userDto.getIsActive() != null) {
            user.setIsActive(userDto.getIsActive());
        }

        user.setDtUpdated(LocalDateTime.now());

        // Lazy initialization in Hibernate workaround
        user = userRepository.save(user);
        user.getPosts().size();
        user.getRoles().size();

        return user;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.getRoles().size();
        if (!user.getUsername().equals(SecurityUtils.getCurrentUserDetails().getUsername()) && !SecurityUtils.hasRole(Role.ADMIN)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getRoles().size();
        return user;
    }
}
