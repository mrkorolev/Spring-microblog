package ru.specialist.spring.controller.advice;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.specialist.spring.controller.UserController;

import javax.persistence.EntityExistsException;

@ControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {

    @ExceptionHandler({
            EntityExistsException.class
    })
    public String userExists(Model model) {
        model.addAttribute("error", "Username already exists");
        return "redirect:/sign-up";
    }
}
