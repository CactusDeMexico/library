package com.pancarte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.pancarte.proxy")
public class LibrarySpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibrarySpringbootApplication.class, args);
    }
}
