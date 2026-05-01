package com.sohyeon.practice.config.security;

import com.sohyeon.practice.config.security.handler.MemberAuthFailureHandler;
import com.sohyeon.practice.config.security.auth.MemberDetailService;
import com.sohyeon.practice.config.security.provider.MemberAuthenticatorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.DispatcherType;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberAuthenticatorProvider memberAuthenticatorProvider;

    // 로그인 기억하기 사용을 위해 MemberAuthenticatorProvider 내부
    // MemberDetailsService 선언
    @Autowired
    MemberDetailService memberDetailService;

    /*  Spring Security 5.7.0 이후로 WebSecurityConfigurerAdapter 지원 안함(Deprecated)
        -> 대신 Bean 을 생성하여 구성하는 기능 도입 */

    /* 1. HttpSecurity, SecurityFilterChain 구현 필요 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(memberAuthenticatorProvider);

        // csrf는 연습용이므로 disabled (상용 시 활성화 필요)
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> cors.disable());

        http.authorizeHttpRequests(auth -> auth
                // Spring Security 6: FORWARD 요청(ViewResolver 렌더링)에도 인증이 걸리므로 허용 필요
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers("/images/**", "/member/login/**", "/error").permitAll()
                .requestMatchers("/member/**").hasRole("MEMBER")
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/member/login/loginForm")
                .loginProcessingUrl("/member/login/login")
                .defaultSuccessUrl("/member/main")
                .failureHandler(new MemberAuthFailureHandler())
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/member/login/loginForm")
                .deleteCookies("JSESSIONID")
        );

        http.rememberMe(remember -> remember
                .key("deeproot")
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                .userDetailsService(memberDetailService)
                .rememberMeParameter("remember-me")
        );

        return http.build();
    }

    // Q: anyMatchers 에러나는 이유?
    // -> anyMatchers 는 spring security 6.0에서 Deprecated 될 예정이기 때문에 사용할 수 없음
    //    대신 requestMatchers 사용 가능
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // security 에 걸리지 않을 url(요청)들 적기
//        // 정적자원에 대해서는 Security를 설정하지 않도록 함 (img, js 등 생기면 추가 필요)
//        return (web) -> web.ignoring().requestMatchers("/resource/**");
//
//    }


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

