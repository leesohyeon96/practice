package com.sohyeon.practice.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/login" )
    public String login(Model model) {
        return "login";
    }
}
