package ru.specialist.spring.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.specialist.spring.controller.api.CommentApiController;
import ru.specialist.spring.controller.api.PostApiController;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {
        PostApiController.class,
        CommentApiController.class
})
public class ApiControllerAdvice {

    @ExceptionHandler({
            NoSuchElementException.class,
            EntityNotFoundException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFound() {
        return ResponseEntity.notFound().build();
    }
}
