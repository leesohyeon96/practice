package com.sohyeon.practice.controller;

import com.sohyeon.practice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Member;

@Controller
@RequestMapping("/member")
public class MemberController {

    private MemberService service;

    @Autowired
    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String login(@ModelAttribute ModelAndView mv, Member member) {
        service.login(member);

        // modelAndView 로 RETURN하기
        return "login";
    }
    
    // /member/home 만들어야함
}
