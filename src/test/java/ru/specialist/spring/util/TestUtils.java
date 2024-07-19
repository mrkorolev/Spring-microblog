package ru.specialist.spring.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestUtils {

    public static final String API_URL = "http://localhost:8080/spring-microblog/api/1.0";

    public static MultiValueMap<String, String> getHeaders(String login, String password) {
            String auth = login + ":" + password;
            byte[] bytes = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.set(HttpHeaders.AUTHORIZATION, "Basic " + new String(bytes, StandardCharsets.UTF_8));

            return headers;
    }

    public static String asString(Resource resource) {
        try(Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
