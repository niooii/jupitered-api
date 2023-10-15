package com.example.niooii.jupitered;

import java.util.concurrent.atomic.AtomicLong;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JupiterController {

    private final AtomicLong id = new AtomicLong(); //do stuff later

    @GetMapping("/jupiter")
    public JupiterSession startSession(@RequestParam(value = "user") String user,
                             @RequestParam(value = "password") String password,
                             HttpServletRequest request) throws InterruptedException {
        return new JupiterSession(user,
                password);
    }
}