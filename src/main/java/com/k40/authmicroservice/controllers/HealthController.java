package com.k40.authmicroservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/actuator/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\": \"UP\"}");
    }

    @GetMapping("/actuator/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("{\"name\": \"authmicroservice\", \"version\": \"0.0.1\"}");
    }
}
