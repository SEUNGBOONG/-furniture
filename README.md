# furniture


## 🔐 JWT 인증 구조

| 계층             | 패키지 경로                                        | 클래스/인터페이스명                   | 설명 |
|------------------|----------------------------------------------------|----------------------------------------|------|
| **Controller**   | `com.example.demo.auth.controller`                 | `AuthController`                       | 로그인/회원가입 및 토큰 발급 API |
| **DTO**          | `com.example.demo.auth.dto`                        | `LoginRequest`, `LoginResponse`        | 로그인 요청/응답 DTO |
|                  |                                                    | `TokenResponse`                        | Access/Refresh 토큰 응답 DTO |
| **Service**      | `com.example.demo.auth.service`                    | `AuthService`                          | 인증 로직 처리 (비밀번호 검증, 토큰 발급 등) |
|                  |                                                    | `TokenService`                         | JWT 토큰 생성/검증 처리 |
| **Security**     | `com.example.demo.auth.jwt`                        | `JwtTokenProvider`                     | JWT 생성 및 파싱 로직 |
|                  |                                                    | `JwtAuthenticationFilter`              | 요청 시 토큰 유효성 검증 및 인증 객체 생성 |
|                  |                                                    | `JwtProperties`                        | JWT 관련 설정값 (시크릿 키, 만료시간 등) |
| **Config**       | `com.example.demo.auth.config`                     | `SecurityConfig`                       | Spring Security 설정 (필터 적용 등) |
| **CustomAuth**   | `com.example.demo.auth.userdetails`                | `CustomUserDetails`, `UserDetailsServiceImpl` | 인증을 위한 사용자 정보 제공 |
