package ru.specialist.spring.controller.advice;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.specialist.spring.controller.CommentController;
import ru.specialist.spring.controller.PostController;
import ru.specialist.spring.controller.UserController;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

// Catches errors, and allows used to process them any way we want
@ControllerAdvice(assignableTypes = {
        PostController.class,
        UserController.class,
        CommentController.class
})
public class NotFoundAdvice {

    @ExceptionHandler({
            NoSuchElementException.class,
            EntityNotFoundException.class,
            UsernameNotFoundException.class
    })
    public String handleNotFound() {
        return "not_found";
    }
}
