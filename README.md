# 🧑‍⚖️ LawMate Spring 협업 가이드

Spring Boot 기반으로 진행되는 본 프로젝트의 팀 개발 시 협업 컨벤션입니다.

---

## 📁 패키지 구조 규칙

```
com.project.lawmate
├── controller        # REST API 컨트롤러
├── domain
│   ├── entity        # JPA 엔티티 클래스
│   └── repository    # JPA 레포지토리 인터페이스
├── dto               # 요청/응답 DTO
├── service           # 비즈니스 로직
├── config            # 설정 클래스 (Security, Swagger 등)
└── exception         # 커스텀 예외, 에러 응답 핸들러
```

---

## 📌 DTO 명명 규칙

| 유형     | 형식            | 예시                        |
|--------|----------------|-----------------------------|
| 요청 DTO | XxxRequest     | `LawQueryRequest`           |
| 응답 DTO | XxxResponse    | `LawSearchResponse`         |
| 내부 DTO | XxxDto         | `LawMatchDto`, `UserInfoDto` |

---

## 🔐 인증 방식

- JWT 기반 인증 사용
- 요청 헤더 형식:
  ```http
  Authorization: Bearer <ACCESS_TOKEN>
  ```

---

## 🧾 공통 API 응답 포맷

```json
{
  "status": 200,
  "message": "성공",
  "data": {
    ...
  }
}
```

- 예외는 `@ControllerAdvice` 전역 처리
- `ApiResponse<T>` 형태로 통일

---

## 🚀 브랜치 전략

| 브랜치명         | 용도            |
|----------------|----------------|
| `main`         | 운영 배포용       |
| `dev`          | 통합 개발 브랜치   |
| `feature/xxx`  | 기능별 브랜치     |

> 예: `feature/law-search`, `feature/gpt-refine`

---

## 💬 커밋 메시지 컨벤션 (Conventional Commits)

| 태그     | 설명                         |
|--------|----------------------------|
| feat   | 새로운 기능 추가                |
| fix    | 버그 수정                     |
| refactor | 리팩토링 (기능 변화 없음)       |
| docs   | 문서 추가/수정                |
| test   | 테스트 코드 관련               |
| chore  | 설정, 빌드파일, 기타 변경사항 등 |

> 예: `feat: 법령 검색 기능 추가`

---

## 🧪 Swagger 설정

- 의존성: `springdoc-openapi-ui`
- 접근 경로: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔐 환경 변수 예시 (.env 또는 application.yml)

```yaml
openai:
  api-key: ${OPENAI_API_KEY}

law:
  api-url: ${LAW_API_URL}
```

- `.env`는 Git에 업로드하지 않음 (`.gitignore` 필수)

---

## 🔧 협업 툴

| 툴         | 용도                 |
|------------|--------------------|
| Notion     | 회의록, 명세서, 일정 정리   |
| Postman    | API 테스트 및 문서 공유    |
| ERDCloud   | DB 모델링           |
| GitHub     | 코드 버전 관리 및 PR 리뷰  |

---

## 📅 작업 규칙

- 코드 작성 전 `feature` 브랜치 생성 후 PR
- 기능 완료 후 `dev` 브랜치로 merge
- 주 1회 정기 미팅 & 코드 리뷰
- 주요 API 또는 로직은 **Swagger 문서화 or README에 예시 추가**

---

## ✅ 프로젝트 핵심 컨벤션 요약

- DTO 명확하게 나누기 (Request/Response/Dto)
- API 응답 형식 통일
- JWT 인증 통일
- 브랜치/커밋 메시지 규칙 따르기
- Swagger & Postman 문서화 병행

---

🧠 *협업을 위한 기본은 “명확한 문서화와 일관된 규칙”입니다. 변경사항이 있으면 항상 README를 업데이트해주세요!*
