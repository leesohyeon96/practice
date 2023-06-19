package com.sohyeon.practice.config.security.auth;

import com.sohyeon.practice.entity.MemberEntity;
import com.sohyeon.practice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// UserDetailsService 를 구현한 클래스
// 스프링 시큐리티가 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
// password 부분 처리는 알아서 함
@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    // JPARepository 를 상속받은 인터페이스를 자동으로 DI (의존성 주입)
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 넘겨받은 id 로 DB 에서 회원 정보 찾음
        MemberEntity member = memberRepository.findByMemberId(username);

        if(member == null) {
            throw new UsernameNotFoundException(username + "을 찾을 수 없습니다.");
        }

        if(!"Y".equals(member.getEnable())) {
            throw new UsernameNotFoundException("사용할 수 없는 계정입니다.");
        }

        // MemberDetails에 Member 객체를 넘겨줌
        return new MemberDetail(member);
    }
}
