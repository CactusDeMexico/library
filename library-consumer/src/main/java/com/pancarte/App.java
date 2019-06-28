package com.pancarte;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;



@SpringBootApplication

public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

