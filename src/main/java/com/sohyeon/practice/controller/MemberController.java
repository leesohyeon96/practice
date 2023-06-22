package com.sohyeon.practice.controller;

import com.sohyeon.practice.config.security.auth.MemberDetail;
import com.sohyeon.practice.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")

public class MemberController {

    private MemberService service;

    @Autowired
    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping("/login/loginForm")
    public String login(HttpServletRequest request,
                        @AuthenticationPrincipal MemberDetail memberDetail) {
        // AuthenticationPrincipal 이란?
        //




        return "login/login";
    }
    
    // /member/home 만들어야함
}
