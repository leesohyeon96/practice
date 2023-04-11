package com.sohyeon.practice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Spring Security 5.7.0 이후로 WebSecurityConfigurerAdapter 지원 안함(Deprecated)
    // 1. 대신 Bean 을 생성하여 구성하는 기능 도입

    // 1-1. HttpSecurity, SecurityFilterChain 구현 필요
    // TODO 구현하기
}
