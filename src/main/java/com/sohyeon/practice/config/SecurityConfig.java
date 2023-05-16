package com.sohyeon.practice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

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
        http.csrf().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/manager/**").hasAnyRole("ADMIN or MANAGER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin().disable() //spring security 에서 제공하는 기본 로그인 페이지가 노출되지 않고 바로 접근이 가능!
                .httpBasic().disable();

                return http.build();


        //  http.csrf(AbstractHttpConfigurer::disable)
        //        .headers(headers -> headers
        //            .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        //        .authorizeRequests(authorize -> authorize
        //            .antMatchers("/user").hasRole("USER")
        //            .anyRequest().authenticated())

//        http.formLogin()
//                .loginPage("/login")                    // localhost:8080 으로 접근햇을 때 이 url 을 따라감
//                .defaultSuccessUrl("/home")
//                .failureUrl("/login")
//                .usernameParameter("userId")
//                .passwordParameter("userPwd")
//                .loginProcessingUrl("/login")
//                .successHandler(
//                        (request, response, authentication) -> {
//                            System.out.println("authentication : " + authentication.getName());
//                            response.sendRedirect("/"); // 인증이 성공한 후에는 root 로 이동
//                        }
//                )
//                .failureHandler(
//                        (request, response, exception) -> {
//                            System.out.println("exception : " + exception.getMessage());
//                            response.sendRedirect("/login");
//                        }
//                )
//                .permitAll();



        // TODO - logout 설정
//        http.logout();

    }

    // Q: anyMatchers 에러나는 이유 알아보기
    // -> anyMatchers 는 spring security 6.0에서 Deprecated 될 예정이기 때문에 사용할 수 없음
    //    대신 requestMatchers 사용 가능
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // security 에 걸리지 않을 url(요청)들 적기
        return (web) -> web.ignoring().requestMatchers("/", "/ignore2");
    }

    // user, admin 아이디인 경우?
    // TODO - 뭔지 알아보기
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("password")
//                .roles("USER", "ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    // 암호 인코딩
    // Springboot 2.0부터 반드시 지정해야 함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
