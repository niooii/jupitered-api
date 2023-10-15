package com.example.niooii;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello %s, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "user") String name,
                             @RequestParam(value = "msg", defaultValue = "inser message here") String msg,
                             HttpServletRequest request) {
        System.out.println(Arrays.toString(request.getCookies()));
        System.out.println(request.getRemoteUser());
        System.out.println(request.getRequestURL() + "?" + request.getQueryString());
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name, msg),
                request.getRemoteAddr());
    }
}