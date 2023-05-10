package com.internship.deltasmartsoftware.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class demorestcontroller {

    @GetMapping("api/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("OK");
    }
}
