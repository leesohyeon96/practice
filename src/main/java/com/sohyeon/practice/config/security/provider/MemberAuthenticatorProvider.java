package com.sohyeon.practice.config.security.provider;

import com.sohyeon.practice.config.security.auth.MemberDetail;
import com.sohyeon.practice.config.security.auth.MemberDetailService;
import com.sohyeon.practice.entity.MemberEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//AuthenticationProvider를 구현한 클래스
// what is AuthenticationProvider?
// - Spring Security 는 내부에 인증절차가 이미 구현되 있는데 그것이 바로 AuthenticationProvider
//   인증 절차를 정의하는 객체
// [참고 설명] https://gregor77.github.io/2021/05/18/spring-security-03/
@Component
@Slf4j
public class MemberAuthenticatorProvider implements AuthenticationProvider {

    @Autowired
    private MemberDetailService memberDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName(); // 사용자가 입력한 id
        String password = (String) authentication.getCredentials(); // 사용자가 입력한 password

        // 생성해둔 MemberDetail 에서 loadUserByUsername 메소드를 호출하여 사용자 정보를 가져온다.
        MemberDetail memberDetail = (MemberDetail) memberDetailService.loadUserByUsername(username);

        // ============= 비밀번호 비교 ==================
        // 사용자가 입력한 password 와 DB 에 저장된 password 를 비교

        // db에 저장된 password
        String dbPassword = memberDetail.getPassword();
        // 암호화 방식(BCryptPasswordEncoder) 를 사용하여 비밀번호를 비교
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(!passwordEncoder.matches(password, dbPassword)) {
            log.debug("[시스템] 비밀번호가 일치하지 않습니다.");
            throw new BadCredentialsException("[시스템] 비밀번호가 일치하지 않습니다.");
        }

        // 사용자가 입력한 id 와 password 가 일치하면 인증이 성공한 것이다.
        // 인증이 성공하면 MemberPrincipalDetails 객체를 반환한다.
        MemberEntity member = memberDetail.getMemberEntity();
        if(member == null || "N".equals(member.getEnable())) {
            log.debug("[시스템] 사용할 수 없는 계정입니다.");
            throw new BadCredentialsException("[시스템] 비밀번호가 일치하지 않습니다.");
        }

        // 인증이 성공하면 UsernamePasswordAuthenticationToken 객체를 반환한다.
        // 해당 객체는 Authentication 객체로 추후 인증이 끝나고 SecurityContextHolder.getContext() 에 저장된다.

        return new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
