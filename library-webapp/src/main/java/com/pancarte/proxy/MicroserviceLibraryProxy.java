package com.pancarte.proxy;

import com.pancarte.Model.Book_List;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import java.util.List;

@FeignClient(name = "microservice-library", url = "http://localhost:9090", decode404 = true)
public interface MicroserviceLibraryProxy {

    @GetMapping(value = "/test")
    String test();

    @GetMapping(value = {"/user/{email}"})
    String queryUser(@PathVariable("email") String email);

    @GetMapping(value = "/role/{email}")
    String queryRole(@PathVariable("email") String email);

    @GetMapping(value = "/search/{search}")
    List<Book_List> searchAvailableBooks(@PathVariable("search") String search);
}
