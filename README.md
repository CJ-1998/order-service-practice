# AI 검증 비즈니스 프로젝트

<br>

## 프로젝트 설명
- GEMINI API 를 기반으로 가게 사장님들의 가게 설명을 요약해 작성을 도와주는 주문 서비스 개발
- 개발기간 : `2024/8/22 ~ 2024/9/2`
- 팀원 : 백엔드 3명(백오피스 개발)
<br>

## 팀원 역할 분담
|이름|역할|
|----|----|
|김건우|가게, 가게 카테고리, 카테고리, 지역 관련 API 개발, 프로젝트 초기 설정, 배포|
|이미연|주문, 상품 주문, 상품, 리뷰 관련 API 개발, 백오피스 개발|
|최준|AI, 사용자, 결제 내역 관련 API 개발, Redis 사용 캐싱 구현|

<br>

## 서비스 구성
<img width="1132" alt="image" src="https://github.com/user-attachments/assets/23730ed8-0b15-4656-b392-1cc797c955a3">


## 실행 방법
> application.yml
```java
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://${POSTGRESQL_URL}:${POSTGRESQL_PORT}/${POSTGRESQL_DB_NAME}
    username: ${POSTGRESQL_USERNAME}
    password: ${POSTGRESQL_PASSWORD}

  jpa:
    properties:
      hibernate:
        format_sql: true
        highlight_sql: true
        hbm2ddl.auto: create
        default_batch_fetch_size: 100
    open-in-view: false
    show-sql: true

  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      username: ${REDIS_USERNAME}
      password: ${REDIS_PASSWORD}

management:
  endpoints:
    web:
      exposure:
        include: health, info

gemini:
  api:
    url: ${GEMINI_URL}
    key: ${GEMINI_KEY}

jwt:
  secret:
    key: ${JWT_SECRET_KEY}
```

<br>

## ERD
![image](https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/8a4212f4-d85a-4673-b850-a7a4e7f06328/%EC%8A%A4%ED%94%84%EB%A7%81%EC%8B%AC%ED%99%94_AI_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8_.png?table=block&id=ff527a9a-76aa-40e7-80d5-f51fecb7d58c&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1725285600000&signature=8x65sfoa21lQ_08WWkkUyH91Ns7wQQehzF07Fl94SPw&downloadName=%EC%8A%A4%ED%94%84%EB%A7%81%EC%8B%AC%ED%99%94+AI+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8+.png)

<br>

ERD 주소 : [ERD](https://www.erdcloud.com/d/o8NWFRjC3BCeYtasL)

<br>

## 기술 스택
![image](https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/3f3c3f0d-c179-48ff-b273-eed237c324dc/image.png?table=block&id=136ecc3c-bc44-4921-aba5-8bdf0919aa52&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1725292800000&signature=sBfrz5btucQAYZPjEp9xLXqOuiI3JScKbisCmj2RJZ0&downloadName=image.png)

<br>

## API docs
API docs 주소 : [API docs](https://www.notion.so/teamsparta/API-0191bf4047484aadbe49e6a5ec30266c)

## :construction: Commit Convention
- init:	branch 추가시 초기 설정
- setup:	프로젝트 세팅
- add:	새로운 기능 추가
- fix:	코드 수정
- bug:	버그 수정
- docs:	문서 수정
- style:	코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우
- refactor:	코드 리팩토링
- test:	테스트 코드, 리팩토링 테스트 코드 추가
- chore:	패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore
- design:	CSS 등 사용자 UI 디자인 변경
- comment:	필요한 주석 추가 및 변경
- rename:	파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우
- remove:	파일을 삭제하는 작업만 수행한 경우
- !BREAKING:	커다란 API 변경의 경우
- !HOTFIX:	급하게 치명적인 버그를 고쳐야 하는 경우
