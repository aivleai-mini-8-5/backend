# 📚 **AIVLE Book Cover Generate Service — Backend**

AI 기반 책 표지 생성과 간단한 도서 관리 기능을 제공하는 **Spring Boot 3 백엔드 서비스**입니다.
JWT 인증 기반 회원 시스템, 도서 CRUD, 좋아요, 인기/카테고리별 조회 기능을 제공합니다.

---

## 🚀 **1. 기술 스택**

| 영역         | 사용 기술                                     |
| ---------- | ----------------------------------------- |
| Language   | **Java 17**                               |
| Framework  | **Spring Boot 3.5.8**                     |
| Modules    | Spring Web, Spring Data JPA, Validation   |
| Security   | JWT(JJWT), BCrypt(Spring Security Crypto) |
| Database   | H2 (in-memory, dev)                       |
| Build Tool | Gradle                                    |
| 기타         | Lombok, DevTools                          |

---

## ▶️ **2. 실행 방법**

### **사전 준비**

* JDK 17+
* Gradle Wrapper 사용 권장: `./gradlew`

### **실행**

```bash
./gradlew clean build
./gradlew bootRun
```

### **기본 접속 정보**

* **Server**: `localhost:8080`
* **H2 Console**: `/h2-console`

  * JDBC URL: `jdbc:h2:mem:testdb`
  * User: `sa`
  * Password: *(공백)*

---

## ⚙️ **3. 주요 설정**

### **application.yaml**

* H2 인메모리 DB(MySQL 모드)
* JPA `ddl-auto: update`
* `open-in-view: false` (Lazy 로딩 안정성)
* 프론트엔드 CORS 허용: `http://localhost:5173`
* 포트: 8080

### **JWT**

* HS256 시크릿 키 사용
* Access Token 만료: **6시간**
* 현재 프로젝트에서는 **컨트롤러에서 직접 파싱** (추후 필터 적용 가능)
* 운영 환경에서는 반드시 **환경 변수로 분리 필요**

---

## 🗂️ **4. 프로젝트 구조**

```
src/main/java/com/aivle/spring
├── Application.java
│
├── common
│   └── ApiResponse.java               # 성공/실패 공통 응답 형식
│
├── config
│   ├── SecurityConfig.java            # BCrypt PasswordEncoder Bean
│   └── WebConfig.java                 # CORS 설정
│
├── domain                              # JPA Entity
│   ├── Book.java
│   ├── Category.java
│   ├── Like.java
│   └── User.java
│
├── exception
│   ├── CustomException.java
│   └── GlobalExceptionHandler.java    # 공통 예외 처리
│
├── jwt
│   └── JwtUtil.java                   # 토큰 생성·검증 유틸
│
├── repository                          # JPA Repository
│   ├── BookRepository.java
│   ├── LikeRepository.java
│   └── UserRepository.java
│
├── service
│   ├── BookService.java / impl        # 도서 CRUD·좋아요·목록
│   └── UserService.java / impl        # 회원가입/로그인/내 정보
│
└── web
    ├── controller
    │   ├── BookController.java
    │   └── UserController.java
    │
    └── dto                            # 요청/응답 DTO
        ├── book/*.java
        └── user/*.java
```

---

## 📡 **5. API 요약**

### 🔐 **인증**

* 로그인 후 `Authorization: Bearer <JWT>` 방식
* 일부 조회 API는 비로그인 가능

---

### 👤 **사용자(User)**

| Method | Endpoint        | 설명              |
| ------ | --------------- | --------------- |
| POST   | `/users/signup` | 회원가입            |
| POST   | `/users/login`  | 로그인 → JWT 발급    |
| GET    | `/users`        | 내 정보 조회         |
| GET    | `/users/books`  | 내가 등록한 도서 목록 조회 |

---

### 📘 **도서(Book)**

| Method | Endpoint          | 설명                      |
| ------ | ----------------- | ----------------------- |
| POST   | `/books`          | 도서 등록                   |
| PUT    | `/books/{bookId}` | 도서 수정 (작성자만)            |
| DELETE | `/books/{bookId}` | 도서 삭제 (좋아요 포함 삭제)       |
| GET    | `/books/{bookId}` | 도서 상세 조회                |
| GET    | `/books`          | 카테고리·정렬(최신/인기) 전체 목록 조회 |
| GET    | `/books/popular`  | 좋아요 상위 10 도서 조회         |

---

### ❤️ **좋아요(Like)**

| Method | Endpoint                | 설명     |
| ------ | ----------------------- | ------ |
| POST   | `/books/{bookId}/likes` | 좋아요 등록 |
| DELETE | `/books/{bookId}/likes` | 좋아요 취소 |

---

### 📦 **응답 형식 (공통 ApiResponse)**

```json
{
  "success": true,
  "message": "요청 성공",
  "data": { ... }
}
```

예외 발생 시 `CustomException` 및 전역 핸들러가 JSON 형식으로 반환합니다.

---
## 📐 **6. 서비스 흐름(Architecture Overview)**
대표 서비스인 신규 도서 등록 API는 사용자가 도서 등록을 요청하면, 프론트엔드(React)가 OpenAI API로 책 표지 이미지를 생성하고 URL을 반환받아
백엔드(Spring Boot)에 도서 정보를 저장하는 흐름입니다.

<img width="459" height="164" alt="image" src="https://github.com/user-attachments/assets/d39dcfa3-e42b-4df0-ac10-1318c030c785" />

---
## 📝 **7. 개발 메모 / 개선 예정**

* 현재 JWT는 Security 필터 없이 직접 파싱 → 추후 Spring Security 적용 가능
* 토큰 키 하드코딩 → 환경변수 기반 설정으로 변경 필요
* 도서 coverUrl 최대 길이 제한 해제 필요 (DB column 길이 확장)
* 테스트 코드 추가 예정 (Service/Repository 단위 테스트)

---

## 👥 **8. 백엔드 파트 구성원**
| 역할              | 이름      | 담당                                                                                    |
| --------------- | ------- | ------------------------------------------------------------------------------------- |
| Backend | **김찬우** | 회원가입/로그인, JWT 인증, 도서 CRUD, 예외 처리 설계 |
| Backend | **박준성** | 도서 좋아요 기능 API                                              |
| Backend | **안지운** | 인기/카테고리 목록 API                                      |
| Backend | **이동욱** | 마이페이지 유저 조회, 유저 도서목록 조회 API                                               |

