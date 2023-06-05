package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainServer {
    public static void main(String[] args) {
        System.getProperties().put("server.port", 8080);
        SpringApplication.run(MainServer.class, args);
    }
}
