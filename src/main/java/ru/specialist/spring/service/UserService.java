package ru.specialist.spring.service;

import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.dto.UserDto;
import ru.specialist.spring.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User findByUsername(String username);

    Long create(UserDto userDto);
    User update(UserDto userDto);
    void delete(Long userId);
}
