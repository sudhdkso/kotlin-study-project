# 프로젝트명 : 게시판
> 이 프로젝트는 코틀린과 코틀린 스프링을 학습하기 위해서 진행하는 간단한 게시판 프로젝트입니다.
## 프로젝트 목표
- 코틀린 학습
- mock
## 📆 진행 기간

---
- 2024.05.22. ~ 05.2x.
## 개발 환경 (Backend)

---
- InteliiJ
- Spring Boot
- gradle
- Spring JPA
## ✅ 기능

---
### 로그인
* [ ]  스프링 시큐리티를 활용한 로그인 기능 구현
* [ ]  테스트 코드 작성 및 통과

### 회원가입
* [ ]  이메일 - 이메일 형식에 맞는지 검증
* [ ]  휴대폰 번호 - 숫자와 하이폰으로 구성된 형식 검즘
* [ ]  작성자 - 아이디 대소문자 및 한글 이름 검즘
* [ ]  비밀번호 - 대소문자, 숫자 5개 이상, 특수문자 포함 2개 이상 검즘

### 게시글 목록 조회
* [x]  생성일 기준 내림차순 오름차순 정렬
  * [x] 게시글 조회 순으로 조회
* [x]  title 기준 부분 검색 가능
* [x]  title 이 없을 경우 cratedAt 정렬 기준으로 표시
* [x]  deletedAt 기준 삭제된 게시글 제외
* [ ]  테스트 코드 작성 및 통과

### 게시글 조회
- 게시글은 boardId를 path로 받아서 조회한다.
- 게시글을 조회하면 조회수가 오른다.
  - 이때 쿠키를 사용하여 조회수를 연속하여 올릴 수 없도록 한다.
* [ ]  수정 가능일 현재 날짜 기준 계산 및 표시
* [ ]  테스트 코드 작성 및 통과
### 게시글 쓰기
- 게시글 작성 시 validation 체크
  * [x]  제목 - 200글자 이하 제한
  * [x]  내용 - 1000글자 이하 제한
  * [x]  생성및 수정 시간 자동 관리
  * [x]  테스트 코드 작성 및 통과

### 게시글 수정
* [x]  생성일 기준 10일 이후 수정불가
* [ ]  생성일 9일째 경고 알림(하루 후 수정 불가 알람)
* [ ]  테스트 코드 작성 및 통과
### 게시글 삭제
* [x]  Soft Delete 적용 deletedAt 사용하여 삭제처리
* [x]  테스트 코드 작성 및 통과

## 🧂 추후

---
- 댓글 기능 
  - 대댓글까지 