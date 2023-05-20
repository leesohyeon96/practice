package com.sohyeon.practice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@Slf4j
// https://jiurinie.tistory.com/70 참고
public class SecurityConfig {
    // Spring Security 5.7.0 이후로 WebSecurityConfigurerAdapter 지원 안함(Deprecated)
    // 1. 대신 Bean 을 생성하여 구성하는 기능 도입

    // 1-1. HttpSecurity, SecurityFilterChain 구현 필요
    // https://velog.io/@seongwon97/Spring-Security-Form-Login 참고
    @Bean // Bean 으로 등록 완료
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        // authorizeHttpRequests -> permitall 사용할 경우 권한은 검증X && 요청을 보호할 수 있어 권장됨

        // TODO - csrf(크로스 사이트 요청 위조) 설정
        // 1. 연습때만 disabled -> 상용일 경우 csrf 공격에 취약
        http.cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
            .and()
//                .formLogin().disable() //spring security 에서 제공하는 기본 로그인 페이지가 노출되지 않고 바로 접근이 가능!
                .formLogin()
                .loginPage("/login")
//                .loginProcessingUrl() // 뭐징
//                .usernameParameter("id")
//                .passwordParameter("pw")
                .defaultSuccessUrl("/")
                .failureForwardUrl("/login")
                .permitAll()
            .and()
                // 1. 로그아웃 시 post 로 요청해야하며, 이 방식으로 요청 시 csrf 토큰도 같이 보냄
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                //세션 공간을 찾기위한 키값! 을 함께 쿠키에 담아서 전달을 해줘야 (header에 달고 요청하면) session공간 찾아갈 수 있게 됨!
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/")
            .and()
                // .exceptionHandling()
                // .accessDeniedPage("/error/denied");
                .httpBasic().disable();

        return http.build();
    }

    // Q: anyMatchers 에러나는 이유?
    // -> anyMatchers 는 spring security 6.0에서 Deprecated 될 예정이기 때문에 사용할 수 없음
    //    대신 requestMatchers 사용 가능
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // security 에 걸리지 않을 url(요청)들 적기
        // 정적자원에 대해서는 Security를 설정하지 않도록 함 (img, js 등 생기면 추가 필요)
        return (web) -> web.ignoring().requestMatchers("/resource/**");

    }

    // 암호 인코딩
    // Springboot 2.0부터 반드시 지정해야 함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // user, admin 아이디인 경우?
    // TODO - 뭔지 알아보기
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("password")
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}
