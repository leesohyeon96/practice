package com.sohyeon.practice.repository;

import com.sohyeon.practice.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

    MemberEntity findByMemberId(String username);
}
