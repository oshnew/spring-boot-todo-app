
# Spring Boot Todo Application

[![Build Status](https://travis-ci.org/akageun/spring-boot-todo-app.svg?branch=master)](https://travis-ci.org/akageun/spring-boot-todo-app)
[![Coverage Status](https://coveralls.io/repos/github/akageun/spring-boot-todo-app/badge.svg?branch=master)](https://coveralls.io/github/akageun/spring-boot-todo-app?branch=master)


이 프로젝트는 Spring Boot로 만들어진 Todo Application 입니다.

## 사용기술
- Jdk 1.8
- Maven 3
- Spring Boot
- Spring Boot JPA
- Thymeleaf 
- H2 Database
- Bootstrap
- Jquery
- lombok

## Features
- 할일을 페이징된 리스트로 조회할 수 있다.
- 할일 등록/수정
- 할일 참조기능(검색기능 포함)
- 할일 완료기능

## 실행방법
1. Clone
```
git clone https://testest.com
```

2. 빌드하기(실행 jar명은 변경 될 수 있습니다. [#17](https://github.com/akageun/spring-boot-todo-app/issues/17))
```
cd spring-boot-backend
(만약 test를 skip하고 싶으면 아래 명령어에 -DskipTests=true 를 붙이면 됨)
mvn package
java -jar target/todoapp-0.1.0.jar 
```

- 위 방법과 다른 실행 방법
```
mvn spring-boot:run
```

## TEST
```
mvn test
```
