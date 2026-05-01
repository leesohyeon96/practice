package com.sohyeon.practice.config.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class MemberAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("loginErrorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
        response.sendRedirect("/member/login/loginForm");
    }
}
