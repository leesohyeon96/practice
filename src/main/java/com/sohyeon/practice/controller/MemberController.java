package com.sohyeon.practice.controller;

import com.sohyeon.practice.config.security.auth.MemberDetail;
import com.sohyeon.practice.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/login/loginForm")
    public String login(HttpServletRequest request,
                        @AuthenticationPrincipal MemberDetail memberDetail) {
        // AuthenticationPrincipal 이란?
        //세션 정보 UserDetails에 접근할 수 있는 어노테이션
        HttpSession session = request.getSession();
        String msg = (String) session.getAttribute("loginErrorMessage");
        session.setAttribute("loginErrorMessage", msg != null? msg : "");

        if(isAuthenticated()) {
            if(memberDetail == null) {
                return "redirect:/member/login/logout";
            }
            return "redirect:/member/main";
        }

        return "login/login";
    }

    @GetMapping("/main")
    public String main() {
        return "main/main";
    }

}
