package com.example.springdoc;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String home(){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return "redirect:/swagger-ui.html";
    }

}
