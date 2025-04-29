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




| 계층             | 패키지 경로                                | 클래스/인터페이스명                         | 설명 |
|------------------|--------------------------------------------|----------------------------------------------|------|
| **Controller**   | `com.example.demo.product.controller`       | `CategoryController`                         | 카테고리 등록/수정/삭제/조회 API |
|                  |                                            | `ProductController`                          | 상품 등록/수정/삭제/조회 API |
| **DTO**          | `com.example.demo.product.controller.dto`   | `CategoryRequest`, `CategoryResponse`        | 카테고리 요청/응답 DTO |
|                  |                                            | `ProductRequest`, `ProductResponse`          | 상품 요청/응답 DTO |
| **Service**      | `com.example.demo.product.service`          | `CategoryService`                            | 카테고리 비즈니스 로직 처리 |
|                  |                                            | `ProductService`                             | 상품 비즈니스 로직 처리 |
| **Entity**       | `com.example.demo.product.domain.entity`    | `Category`                                   | 카테고리 엔티티 |
|                  |                                            | `Product`                                    | 상품 엔티티 |
| **Repository**   | `com.example.demo.product.domain.repository`| `CategoryRepository`                         | JPA 기반 카테고리 CRUD |
|                  |                                            | `ProductRepository`                          | JPA 기반 상품 CRUD |
| **기타**         | `com.example.demo.common`                  | `Setting`                                    | 상수 메시지 정의 |
| **Security**     | `com.example.demo.login.global.annotation`  | `@Member` 애노테이션                         | 사용자 식별용 커스텀 애노테이션 |
