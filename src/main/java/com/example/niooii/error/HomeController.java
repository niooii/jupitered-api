package com.example.niooii.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
@RestController
public class HomeController {

    @GetMapping("/")
    public Home home(HttpServletRequest request) {
        System.out.println(Arrays.toString(request.getCookies()));
        System.out.println(request.getRemoteUser());
        System.out.println(request.getRequestURL() + "?" + request.getQueryString());
        return new Home(150, "brain not found");
    }
}
