package com.sohyeon.practice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "MEMBER")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스 전략 선언
    @Column(name = "MEMBER_SNO")
    private int memberSno;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "MEMBER_PWD")
    private String memberPwd;

    @Column(name = "MEMBER_NM")
    private String memberNm;

    @Column(name = "MEMBER_ROLE")
    private String memberRole;

    @Column(name = "INS_DT")
    private LocalDateTime insDt;

    @Column(name = "UPD_DT")
    private LocalDateTime updDt;
}
