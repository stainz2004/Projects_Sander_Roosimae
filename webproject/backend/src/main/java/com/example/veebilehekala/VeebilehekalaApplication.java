package com.example.veebilehekala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VeebilehekalaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VeebilehekalaApplication.class, args);
    }
}
