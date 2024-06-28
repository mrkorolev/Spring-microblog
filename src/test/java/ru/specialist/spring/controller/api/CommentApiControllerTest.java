package ru.specialist.spring.controller.api;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestOperations;
import ru.specialist.spring.config.DataConfig;
import ru.specialist.spring.dto.CommentDto;
import ru.specialist.spring.repository.CommentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.specialist.spring.util.TestUtils.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataConfig.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CommentApiControllerTest {

    private final Resource findAll;
    private final Resource findById;
    private final Resource findAllForPost;
    private final CommentRepository commentRepository;
    private final RestOperations restTemplate;

    @Autowired
    public CommentApiControllerTest(CommentRepository commentRepository,
                                    RestOperations restTemplate,
                                    @Value("classpath:comments/findAll.json") Resource findAll,
                                    @Value("classpath:comments/findById.json") Resource findById,
                                    @Value("classpath:comments/findAllForPost.json") Resource findAllForPost) {
        this.restTemplate = restTemplate;
        this.commentRepository = commentRepository;
        this.findAll = findAll;
        this.findById = findById;
        this.findAllForPost = findAllForPost;
    }

    @Test
    void findAll() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL + "/comment", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(asString(findAll), response.getBody(), true);
    }

    @Test
    void findById() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL + "/comment/3", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(asString(findById), response.getBody(), true);
    }

    // Retrieve all comments for post with id 1s
    @Test
    void findAllForPost() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL + "/comment/post/1", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(asString(findAllForPost), response.getBody() ,true);
    }

    // Create a new comment for post with id 1
    @Test
    void create() {
        ResponseEntity<Long> response =
                restTemplate.postForEntity(API_URL + "/comment/post/1",
                        new HttpEntity<>(
                                new CommentDto("Another important comment"),
                                getHeaders("matt.walters", "MattWalters")
                        ),
                        Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7L, response.getBody());
        assertEquals("Another important comment", commentRepository.findById(7L).get().getContent());
    }

    @Test
    void delete() {
        restTemplate.exchange(API_URL + "/comment/1",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders("matt.walters", "MattWalters")),
                Void.class);
        assertEquals(5, commentRepository.count());
    }
}
