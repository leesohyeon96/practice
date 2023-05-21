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
// https://reflectoring.io/spring-security/ 참고
// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
// https://nahwasa.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-30%EC%9D%B4%EC%83%81-Spring-Security-%EA%B8%B0%EB%B3%B8-%EC%84%B8%ED%8C%85-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0
public class SecurityConfig {
    /*  Spring Security 5.7.0 이후로 WebSecurityConfigurerAdapter 지원 안함(Deprecated)
        -> 대신 Bean 을 생성하여 구성하는 기능 도입 */

    /* 1. HttpSecurity, SecurityFilterChain 구현 필요 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* authorizeHttpRequests -> permitall 사용할 경우 권한은 검증X && 요청을 보호할 수 있어 권장됨 */

        // TODO - csrf(크로스 사이트 요청 위조) 설정
        /* 1. 연습때만 disabled -> 상용일 경우 csrf 공격에 취약 */
        http.cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/login", "/", "").permitAll()
                .anyRequest().authenticated()
            .and()
            /* spring security 에서 제공하는 기본 로그인 페이지가 노출되지 않고 바로 접근이 가능! */
            //  .formLogin().disable()
                .formLogin()
                .loginPage("/login").permitAll()
//                .loginProcessingUrl() // 뭐징
//                .usernameParameter("admin")
//                .passwordParameter("password26")
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
}

