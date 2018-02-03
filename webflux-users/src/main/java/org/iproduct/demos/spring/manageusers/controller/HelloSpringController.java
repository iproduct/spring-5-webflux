package org.iproduct.demos.spring.manageusers.controller;

import org.iproduct.demos.spring.manageusers.domain.Hello;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HelloSpringController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

//    @RequestMapping(method = RequestMethod.GET)
//    String home() {
//        return "Hello World!";
//    }

    @GetMapping("/hello")
    public @ResponseBody
    Hello sayHello(@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
        return new Hello(counter.incrementAndGet(), String.format(template, name));
    }
}
