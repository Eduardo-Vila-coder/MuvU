package com.example.desarrollo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DesarrolloApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesarrolloApplication.class, args);
    }

}
