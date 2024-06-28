package ru.specialist.spring.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

    // Такой же инициализатор, как и для web
    public SecurityWebAppInitializer() {
        super(WebSecurityConfig.class);
    }


}
