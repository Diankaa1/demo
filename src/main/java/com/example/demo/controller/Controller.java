package com.example.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/hello")
    public String sayHello() {
        return "Привет, мир!";
    }

    @PostMapping("/echo")
    public Message echo(@RequestBody Message input) {
        return input;
    }

    @Data
    @AllArgsConstructor
    static class Message {
        private String text;
    }
}