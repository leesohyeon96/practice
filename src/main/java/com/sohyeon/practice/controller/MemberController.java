package com.sohyeon.practice.controller;

import com.sohyeon.practice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Member;

@Controller
public class MemberController {

    private MemberService service;

    @Autowired
    public MemberController(MemberService service) {
        this.service = service;
    }

    @RequestMapping("/login")
    public String login(Member member) {
        service.login(member);

        // modelAndView 로 RETURN하기
        return "login";
    }
}
