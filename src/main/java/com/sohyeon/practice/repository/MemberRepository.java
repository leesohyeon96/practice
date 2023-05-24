package com.sohyeon.practice.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {
    void join();

    void login();

    void logout();

    void withdraw();

}