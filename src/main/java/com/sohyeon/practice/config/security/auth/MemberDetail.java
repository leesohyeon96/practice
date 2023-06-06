package com.sohyeon.practice.config.security.auth;

import com.sohyeon.practice.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Spring Security 에 있는 UserDetails 를 구현한 클래스,
// 이 클래스를 통해 Spring Security 에서 사용자의 정보를 담아둠
public class MemberDetail implements UserDetails {

    private final MemberEntity memberEntity;

    public MemberDetail(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    // member 계정의 권한을 담아두기 위함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(memberEntity.getMemberRole()));

        return authorityList;
    }

    // member 계정의 비밀번호를 담기 위함
    @Override
    public String getPassword() {
        return memberEntity.getMemberPwd();
    }

    @Override
    public String getUsername() {
        return memberEntity.getMemberId();
    }

    // 계정 만료되지 않았는지를 담아두기 위함(true : 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않았는지를 담아두기 위함(true 만료안됨)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정의 비밀번호가 만료되지 않았는지를 담아두기 위함(true 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화되있는지를 담아두기 위함(true 활성화됨)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
