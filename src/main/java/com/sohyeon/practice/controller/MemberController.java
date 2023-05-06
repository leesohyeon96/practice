package com.sohyeon.practice.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    // login 성공하면 url => "/" , 그러면 index.html로 이동하거나 home.html로 이동하게 하고 싶은데
    @GetMapping("/")
    public String main(Model model) {
        return "home";
    }
}
