package com.internship.deltasmartsoftware.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class demorestcontroller {

    @GetMapping("/")
    public String sayHello() {
        return "Hello World! Time on server is " + java.time.LocalDateTime.now();
    }
}
