package org.iproduct.demos.spring.hellowebflux.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloSpringController {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

}
