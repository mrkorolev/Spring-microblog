package ru.specialist.spring.controller.api;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestOperations;
import ru.specialist.spring.config.DataConfig;
import ru.specialist.spring.dto.PostDto;
import ru.specialist.spring.repository.PostRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.specialist.spring.util.TestUtils.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataConfig.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PostApiControllerTest {

    private final PostRepository postRepository;
    private final RestOperations restTemplate;
    private final Resource findAll;
    private final Resource findById;

    @Autowired
    public PostApiControllerTest(@Value("classpath:posts/findAll.json") Resource findAll,
                                 @Value("classpath:posts/findById.json") Resource findById,
                                 RestOperations restTemplate,
                                 PostRepository postRepository) {
        this.restTemplate = restTemplate;
        this.findAll = findAll;
        this.findById = findById;
        this.postRepository = postRepository;
    }

//    @Test   // HttpClient takes too long, Use WebFlux or RestTemplate
//    void findAll() throws IOException, InterruptedException {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .GET()
//                .uri(URI.create("localhost:8080/spring-security/api/1.0/post"))
//                .build();
//        client.send(request, response -> {
//            System.out.println(response);
//            return null;
//        });
//    }

    @Test
    void findAll() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL + "/post", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(asString(findAll), response.getBody(), true);
    }

    @Test
    void findById() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL + "/post/1", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals(asString(findById), response.getBody(), true);
    }

    @Test
    void create() {
        ResponseEntity<Long> response =
                restTemplate.postForEntity(API_URL + "/post",
                        new HttpEntity<>(
                                new PostDto("Day 4", "Something interesting is about to happen!"),
                                getHeaders("matt.walters", "MattWalters")
                        ),
                        Long.class
                );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4L, response.getBody());
        assertEquals("Day 4", postRepository.findById(4L).get().getTitle());
    }

    @Test
    void update() {
        restTemplate.put(API_URL + "/post/1",
                    new HttpEntity<>(
                            new PostDto("Day 4"),
                            getHeaders("matt.walters", "MattWalters")
                    )
        );
        assertEquals("Day 4", postRepository.findById(1L).orElseThrow().getTitle());
    }

    @Test
    void delete() {
        restTemplate.exchange(API_URL + "/post/1",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders("nikkorolev.25", "NikitaKorolev")),
                Void.class);
        assertEquals(2, postRepository.count());
    }
}
