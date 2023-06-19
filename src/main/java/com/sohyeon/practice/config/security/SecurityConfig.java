package com.sohyeon.practice.config.security;

import com.sohyeon.practice.config.security.handler.MemberAuthFailureHandler;
import com.sohyeon.practice.config.security.auth.MemberDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 롬복을 사용하여 어노테이션으로 생성자 주입 코딩 가능 ㄷㄷ
@Slf4j
// https://nahwasa.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-30%EC%9D%B4%EC%83%81-Spring-Security-%EA%B8%B0%EB%B3%B8-%EC%84%B8%ED%8C%85-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0
// https://dev-log.tistory.com/4 (5.7.0 이후 github 보고 따라해보기..)
public class SecurityConfig {



    // 로그인 기억하기 사용을 위해 MemberAuthenticatorProvider 내부
    // MemberDetailsService 선언
    MemberDetailService memberDetailService;

    /*  Spring Security 5.7.0 이후로 WebSecurityConfigurerAdapter 지원 안함(Deprecated)
        -> 대신 Bean 을 생성하여 구성하는 기능 도입 */

    /* 1. HttpSecurity, SecurityFilterChain 구현 필요 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* authorizeHttpRequests -> permitall 사용할 경우 권한은 검증X && 요청을 보호할 수 있어 권장됨 */

        // TODO - csrf(크로스 사이트 요청 위조) 설정
        /* 1. 연습때만 disabled -> 상용일 경우 csrf 공격에 취약 */
        http.cors().disable().csrf().disable()
                .authorizeHttpRequests(request -> {
                    try {
                        request
                                //   dispatcherTypeMatchers 부분의 설정은
                                //   스프링 부트 3.0부터 적용된
                                //   스프링 시큐리티 6.0 부터 forward 방식 페이지 이동에도
                                //   default로 인증이 걸리도록 변경되어서
                                //   JSP나 타임리프 등 컨트롤러에서 화면 파일명을 리턴해
                                //   ViewResolver가 작동해 페이지 이동을 하는 경우 위처럼 설정을 추가하셔야 함
                                //  .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()

                                .requestMatchers("/images/**", "/member/login/**").permitAll() // 인증 필요 없는 url
                                .requestMatchers("/member/**") // 인증 필요한 url
//                              .anyRequest().authenticated()	// 어떠한 요청이라도 인증필요
                                .hasRole("MEMBER")             // MEMBER의 ROLE을 가진 경우만 인증 가능함
                            .and()
                                .formLogin()
                                .loginPage("/member/login/loginForm")       // 로그인 페이지로 이동하는 url
                                .loginProcessingUrl("/member/login/login")  // 로그인 처리 url
                                .defaultSuccessUrl("/member/home")          // 로그인 성공 후 이동할 url
                                .failureHandler(new MemberAuthFailureHandler())  // 로그인 실패 후 처리한 핸들러
                                .permitAll()
                            .and()
                                .logout()
                                .logoutUrl("/member/logout")
                                .logoutSuccessUrl("/member/login/loginFOrm")    // 로그아웃 성공 후 이동 url
                                .deleteCookies("JSESSIONID");     // 로그아웃 후 쿠키 삭제
//                              .logout(withDefaults());	// 로그아웃은 기본설정으로 (/logout으로 인증해제)
                    } catch (Exception e) {
                        throw  new RuntimeException(e);
                    }
                });

        http.rememberMe()
            .key("deeproot")                             // 인증 토큰 생성시 사용할 키
                .tokenValiditySeconds(60 * 60 * 24 * 7)  // 인증 토큰 유효 시간 (초)
                .userDetailsService(memberDetailService) // 인증 토큰 생성시에 사용할 UserDetailService
                .rememberMeParameter("remembermepr");    // 로그인 페이지에서 사용할 파라미터 이름


//                .authorizeHttpRequests()
//                .requestMatchers("/login", "/", "").permitAll()
//                .anyRequest().authenticated()
//            .and()
//            /* spring security 에서 제공하는 기본 로그인 페이지가 노출되지 않고 바로 접근이 가능! */
//            //  .formLogin().disable()
//                .formLogin()
//                .loginPage("/login").permitAll()
////                .loginProcessingUrl() // 뭐징
////                .usernameParameter("admin")
////                .passwordParameter("password26")
//                .defaultSuccessUrl("/")
//                .failureForwardUrl("/login")
//                .permitAll()
//            .and()
//                // 1. 로그아웃 시 post 로 요청해야하며, 이 방식으로 요청 시 csrf 토큰도 같이 보냄
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                //세션 공간을 찾기위한 키값! 을 함께 쿠키에 담아서 전달을 해줘야 (header에 달고 요청하면) session공간 찾아갈 수 있게 됨!
//                .deleteCookies("JSESSIONID")
//                .invalidateHttpSession(true)
//                .logoutSuccessUrl("/")
//            .and()
//                // .exceptionHandling()
//                // .accessDeniedPage("/error/denied");
//                .httpBasic().disable();

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

//    @Bean //<-- 이 부분을 빼먹어서 '자격 증명에 실패하였습니다'
//    public UserDetailsService userDetailsService() {
//        // UserDetail -> spring security 에서 사용자의 정보를 담는 interface
//        //인메모리에 username, password, role 설정
//        // 1. User vs UserDetail
//        //   - User : UserDetail을 구현해둔 객체(굳이 UserDetails를 구현한 객체를 직접 만들지 않을 때 사용하는 것)
//        //   - UserDetail : 핵심 사용자 정보를 제공하는 interface
//        //     (UserDetailService : 사용자별 데이터를 로드하는 핵심 interface
//        //                          + Spring Security에서 유저의 정보를 가져오는 interface)
//        UserDetails user =
//                User.builder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        System.out.println("password : " + user.getPassword());
//
//        return new InMemoryUserDetailsManager(user);
//    }

}

