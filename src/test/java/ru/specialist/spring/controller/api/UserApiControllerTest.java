package ru.specialist.spring.controller.api;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import ru.specialist.spring.config.DataConfig;
import ru.specialist.spring.dto.UserDto;
import ru.specialist.spring.repository.UserRepository;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.specialist.spring.util.TestUtils.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataConfig.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserApiControllerTest {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final Resource findAll;
    private final Resource findByUsername;

    @Autowired
    public UserApiControllerTest(UserRepository userRepository,
                                 RestTemplate restTemplate,
                                 @Value("classpath:users/findAll.json") Resource findAll,
                                 @Value("classpath:users/findByUsername.json") Resource findByUsername) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.findAll = findAll;
        this.findByUsername = findByUsername;
    }

    // Retrieve all users' info - contains sensitive data, only for admin users
    @Test
    void findAll() throws JSONException {
        ResponseEntity<String> response = restTemplate.exchange(API_URL + "/user",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders("nikkorolev.25", "NikitaKorolev")),
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(asString(findAll), response.getBody(), true);
    }

    // Same principle as above test method
    @Test
    void findByUsername() throws JSONException {
        ResponseEntity<String> response = restTemplate.exchange(API_URL + "/user/ethan.winters",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders("nikkorolev.25", "NikitaKorolev")),
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(asString(findByUsername), response.getBody(), true);
    }

    @Test
    void create() {
        ResponseEntity<Long> response = restTemplate.postForEntity(API_URL + "/user",
                new HttpEntity<>(
                        new UserDto("javier.bardem", "JavierBardem")),
                Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(9L, response.getBody());
        assertEquals("javier.bardem", userRepository.findById(9L).orElseThrow().getUsername());
        assertNotEquals("JavierBardem", userRepository.findById(9L).orElseThrow().getPassword());
    }

    // Updating can only be carried out by admin or user who created the account
    @Test
    void update() {
        ResponseEntity<String> response = restTemplate.exchange(API_URL + "/user/7",
                HttpMethod.PUT,
                new HttpEntity<>(
                        new UserDto(true, "jonathan.byers123"),
                        getHeaders("nikkorolev.25", "NikitaKorolev")),
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jonathan.byers123", userRepository.findById(7L).orElseThrow().getUsername());
        assertTrue(userRepository.findById(7L).orElseThrow().getIsActive());
    }

    // Same logic as above test method
    @Test
    void delete() {
        restTemplate.exchange(API_URL + "/user/8",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders("jessica.alba", "JessicaAlba")),
                Void.class);
        assertEquals(7, userRepository.count());
        assertThrows(NoSuchElementException.class, () -> userRepository.findById(8L).orElseThrow());

        restTemplate.exchange(API_URL + "/user/7",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders("nikkorolev.25", "NikitaKorolev")),
                Void.class);
        assertEquals(6, userRepository.count());
        assertThrows(NoSuchElementException.class, () -> userRepository.findById(7L).orElseThrow());
    }
}
