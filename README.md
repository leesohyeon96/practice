# Practice — Spring Security 학습 프로젝트

Spring Boot 3 기반으로 Spring Security 커스텀 인증 흐름을 학습하는 프로젝트입니다.

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 (런타임 Java 21) |
| Framework | Spring Boot 3.0.5 |
| Security | Spring Security 6 |
| ORM | Spring Data JPA (Hibernate) |
| DB | MariaDB 10.11 |
| View | Thymeleaf |
| 기타 | Lombok 1.18.34, Spring DevTools |

## 주요 구현 내용

- **커스텀 AuthenticationProvider** (`MemberAuthenticatorProvider`) — ID/PW 검증 및 계정 활성화 여부 확인
- **UserDetailsService** (`MemberDetailService`) — DB에서 회원 정보를 로드해 Security 컨텍스트에 주입
- **BCrypt 비밀번호 암호화** — Spring Security 6 필수 PasswordEncoder 설정
- **Remember-me** — 7일 유효 토큰 기반 자동 로그인
- **로그인 실패 핸들러** (`MemberAuthFailureHandler`) — 실패 메시지 세션 처리 후 로그인 페이지로 리다이렉트

## URL 구조

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| GET | `/member/login/loginForm` | 로그인 페이지 | 불필요 |
| POST | `/member/login/login` | 로그인 처리 | 불필요 |
| GET | `/member/main` | 메인 페이지 | `ROLE_MEMBER` |
| GET | `/member/logout` | 로그아웃 | - |

## DB 스키마

```sql
CREATE TABLE MEMBER (
    MEMBER_SNO  INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    MEMBER_ID   VARCHAR(100) NOT NULL,
    MEMBER_PWD  VARCHAR(255) NOT NULL,  -- BCrypt 해시
    MEMBER_NM   VARCHAR(100),
    MEMBER_ROLE VARCHAR(50),            -- ROLE_MEMBER
    INS_DT      DATETIME,
    UPD_DT      DATETIME,
    ENABLE      CHAR(1)                 -- Y: 활성, N: 비활성
);
```

## 로컬 실행

**1. MariaDB 컨테이너 시작**

```bash
docker-compose up -d
```

컨테이너 기동 시 `docker/init.sql`이 자동 실행되어 테이블 생성 및 테스트 계정이 INSERT됩니다.

**2. Spring Boot 앱 시작**

IntelliJ에서 `PracticeApplication.java`를 열고 실행(▶)합니다.
콘솔에 아래 로그가 나오면 정상 기동된 것입니다.

```
Started PracticeApplication in X.XXX seconds
```

**3. 브라우저 접속**

```
http://localhost:8080/member/login/loginForm
```

| 항목 | 값 |
|------|----|
| 테스트 ID | `test` |
| 테스트 PW | `1234` |
