## 프로젝트명 : 게시판
> 이 프로젝트는 코틀린과 코틀린 스프링을 학습하기 위해서 진행하는 간단한 게시판 프로젝트입니다.

### 📆 진행 기간

---
- 2024.05.22. ~ 05.2x.
### 개발 환경 (Backend)

---
- InteliiJ
- Spring Boot
- gradle
- Spring JPA
### ✅ 기능

---
- 사용자
  - [x] 사용자 등록
- 게시글
  - [x] 게시글 목록 조회
    - 게시글 작성 일자 순으로 조회
    - 게시글 조회 순으로 조회
  - [x] 게시글 조회 
    - 게시글은 boardId를 path로 받아서 조회한다.
    - 게시글을 조회하면 조회수가 오른다.
      - 이때 쿠키를 사용하여 조회수를 연속하여 올릴 수 없도록 한다.
  - [x] 게시글 쓰기
    - 게시글 작성 시 validation 체크
      - 제목의 길이는 최소 1에서 최대 20까지이다.
      - 내용의 길이는 최소 1에서 최대 Int.MAX_VALUE이다.
  - [x] 게시글 수정
  - [x] 게시글 삭제
  - [ ] 게시글 검색
    - 검색어는 2자 이상

### 🧂 추후

---
- 댓글 기능 
  - 대댓글까지 depth 2 