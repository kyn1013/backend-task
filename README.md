## 🗝️ JWT를 활용한 인증/인가 및 권한 구현 프로젝트

## 1. 프로젝트 개요

개발 기간 : 2025.06.09 - 2025.06.11

이 프로젝트는 Spring Boot 기반 인증 시스템을 구축하는 과제로, Spring Security와 JWT를 활용한 보안 인증과 역할 기반 접근 제어 기능을 포함합니다. 모든 데이터는 인메모리 구조로 처리됩니다. 만들어진 API에 대해 성공, 실패 및 토큰 인증에 관한 테스트코드를 JUnit을 기반으로 작성했습니다. 또한 Swagger UI를 통해 API 명세와 테스트가 가능하며, Amazon EC2에 배포하여 외부에서도 API를 호출할 수 있도록 구성했습니다.

## 2. 프로젝트 주요 기능


1️⃣ **회원가입 및 로그인 API**

- 일반 사용자(ROLE_USER)와 관리자(ROLE_ADMIN) 모두 가입 및 로그인 가능

2️⃣ **JWT 기반 인증 처리**

- 로그인 시 Access Token 발급
- 인증이 필요한 요청에 JWT 검증 필터 적용

3️⃣ **JWT 예외 처리**

- JWT 관련 예외 상황에 대한 공통 응답 처리

  - JWT 토큰 없음 (TOKEN_NOT_FOUND)
  -  만료된 JWT (EXPIRED_JWT_TOKEN)
  - 유효하지 않은 JWT 서명 (INVALID_SIGNATURE)
  -  잘못된 형식의 JWT (MALFORMED_JWT_TOKEN)
  -  지원되지 않는 JWT (UNSUPPORTED_JWT_TOKEN)

4️⃣ **Role 기반 권한 관리**

- 관리자(ROLE_ADMIN)만 접근 가능한 관리자 전용 API 구현
  - 권한 부여 API(http://43.201.8.178:8080/api/v1/admin/roles) : 특정 사용자에게 권한을 추가로 부여
  - 전체 회원 정보 조회 API (http://43.201.8.178:8080/api/v1/admin/users): 모든 회원의 정보를 조회

## 3. Swagger 주소 - http://43.201.8.178:8080/swagger-ui/index.html#/
- ‼️ **회원가입 테스트 시 이메일을 예시에 나온 이메일 말고 다른걸로 진행해주세요** ‼️
- Swagger UI를 통해 모든 API를 손쉽게 테스트할 수 있습니다.
- JWT 인증이 필요한 API는 Swagger UI에서 "Authorize" 버튼을 클릭하여 토큰 입력 후 테스트 가능합니다.

## 4. EC2에서 실행 중인 API 엔드포인트 URL
| 이름               | 메서드 | URL                         |권한|
|--------------------|--------|------------------------------|----|
| 회원가입           | POST   | `http://43.201.8.178:8080/api/v1/auth/signup`       |   |
| 로그인             | POST   | `http://43.201.8.178:8080/api/v1/auth/signin`       |  |
| 전체 회원 조회     | GET    | `http://43.201.8.178:8080/api/v1/admin/users`       | ROLE_ADMIN|
| 권한 부여          | PATCH   | `http://43.201.8.178:8080/api/v1/admin/roles`   | ROLE_ADMIN|
