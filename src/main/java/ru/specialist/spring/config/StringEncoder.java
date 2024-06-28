package ru.specialist.spring.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Scanner;

public class StringEncoder {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try(Scanner scanner = new Scanner(System.in)){
            String s = scanner.nextLine();
            while(!Objects.equals(s, "q")){
                System.out.println(encoder.encode(s));
                s = scanner.nextLine();
            }
        }
    }
}
