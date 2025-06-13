# furniture

Front - End Git hub Repository : https://github.com/hyunyeee/furniture

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


🚀 성능 개선: 병목 현상 제거를 위한 쿼리 최적화
📌 문제 상황
기존에는 JPA의 기본 메서드를 활용해 장바구니 데이터를 처리하고 있었습니다. 하지만 다음과 같은 문제들이 발생했습니다:

N+1 문제로 인해 다수의 쿼리 호출 발생

수량 변경 시, 여러 번의 DB 호출

Member → Product → Cart 간 조인으로 인한 복잡한 쿼리 실행

API 응답 속도 측정 결과, TTFB(Time To First Byte)가 3.17초에 달함 → 사용자 경험 악화

🛠 해결 방법
JPA 메서드 호출 방식 대신, 직접 쿼리를 작성하여 데이터를 한 번에 처리하도록 개선했습니다:

updateQuantityByAmount() : 수량 증감을 단일 쿼리로 처리

existsCartItem() : 이미 담긴 상품 여부를 불필요한 엔티티 조회 없이 판단

upsertCartItem() : 있으면 갱신, 없으면 추가하는 방식으로 쿼리 최적화

✅ 성과
TTFB 기준 3.17s → 약 500ms 수준으로 단축

DB 호출 횟수 및 트랜잭션 수 감소

복잡한 조인 구조 제거로 쿼리 실행 시간 개선

사용자 경험 및 시스템 부하 모두 개선

