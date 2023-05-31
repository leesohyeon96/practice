package com.sohyeon.practice.config;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
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

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@Slf4j
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
        http.cors().disable().csrf().disable()
                .authorizeHttpRequests(request -> request
                        //   dispatcherTypeMatchers 부분의 설정은 
                        //   스프링 부트 3.0부터 적용된 
                        //   스프링 시큐리티 6.0 부터 forward 방식 페이지 이동에도 
                        //   default로 인증이 걸리도록 변경되어서
                        //   JSP나 타임리프 등 컨트롤러에서 화면 파일명을 리턴해 
                        //   ViewResolver가 작동해 페이지 이동을 하는 경우 위처럼 설정을 추가하셔야 함
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/status", "/images/**", "/join").permitAll()
                        .anyRequest().authenticated()	// 어떠한 요청이라도 인증필요
                )
                .formLogin(login -> login	// form 방식 로그인 사용
//                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)	// 성공 시 home으로
                        .permitAll()	// 이동이 막히면 안되므로 얘는 허용
                )

                /* 언젠가 login 커스텀 ㄱㄱ */
//                .formLogin(login -> login
//                        .loginPage("/login")	// [A] 커스텀 로그인 페이지 지정
//                        .loginProcessingUrl("/login-process")	// [B] submit 받을 url
//                        .usernameParameter("userid")	// [C] submit할 아이디
//                        .passwordParameter("pw")	// [D] submit할 비밀번호
//                        .defaultSuccessUrl("/home", true)
//                        .permitAll()
//                )

                .logout(withDefaults());	// 로그아웃은 기본설정으로 (/logout으로 인증해제)


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

    @Bean //<-- 이 부분을 빼먹어서 '자격 증명에 실패하였습니다'
    public UserDetailsService userDetailsService() {
        // UserDetail -> spring security 에서 사용자의 정보를 담는 interface
        //인메모리에 username, password, role 설정
        // 1. User vs UserDetail
        //   - User : UserDetail을 구현해둔 객체(굳이 UserDetails를 구현한 객체를 직접 만들지 않을 때 사용하는 것)
        //   - UserDetail : 핵심 사용자 정보를 제공하는 interface
        //     (UserDetailService : 사용자별 데이터를 로드하는 핵심 interface
        //                          + Spring Security에서 유저의 정보를 가져오는 interface)
        UserDetails user =
                User.builder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        System.out.println("password : " + user.getPassword());

        return new InMemoryUserDetailsManager(user);
    }

}

