# 애플리케이션 접속 주소 콘솔 출력 설계

## 목표

IntelliJ IDEA Community의 Gradle `bootRun`을 더블클릭해 애플리케이션을 실행하면, 시작 로그의 맨 끝에서 게시판 접속 주소를 바로 확인할 수 있게 한다.

## 현재 흐름

애플리케이션 시작이 완료되면 `PostConsoleRunner`가 Supabase의 게시글을 조회해 콘솔에 출력한다. 현재는 게시글 목록이 마지막 출력이며, 사용자는 별도로 포트와 게시판 경로를 조합해야 한다.

## 결정

접속 주소 출력 책임을 별도 `ApplicationUrlConsoleRunner` 컴포넌트로 분리한다.

- `PostConsoleRunner`를 첫 번째 시작 작업으로 지정한다.
- `ApplicationUrlConsoleRunner`를 그 다음 시작 작업으로 지정한다.
- Spring Boot가 실제로 실행한 웹 서버의 포트를 읽는다.
- 게시글 전체 조회가 끝난 다음 게시판 주소를 한 번만 출력한다.
- 기본 포트 `8080`을 코드에 고정하지 않는다.

이 방식은 게시글 조회 코드와 실행 안내 코드의 책임을 분리하면서도 출력 순서를 명확하게 보장한다.

## 구성 요소

### `PostConsoleRunner`

기존 게시글 전체 조회와 출력 동작을 유지한다. 시작 작업 순서만 첫 번째로 명시한다.

### `ApplicationUrlConsoleRunner`

`com.study.jpalab.config` 패키지에 추가한다.

- Spring Boot의 `WebServerApplicationContext`를 주입받는다.
- `getWebServer().getPort()`로 실제 실행 포트를 가져온다.
- `PostConsoleRunner` 다음 순서로 실행한다.
- 다음 형식으로 출력한다.

```text
=== 애플리케이션 접속 주소 ===
게시판: http://localhost:8080/posts
```

실행 포트가 변경되면 `8080` 부분은 실제 포트로 자동 변경된다.

## 실행 흐름

1. Spring Boot와 Tomcat이 시작된다.
2. `PostConsoleRunner`가 `posts` 테이블을 조회해 게시글을 출력한다.
3. `ApplicationUrlConsoleRunner`가 실제 Tomcat 포트를 읽는다.
4. 콘솔의 마지막에 게시판 접속 주소가 출력된다.

게시글이 한 건도 없어도 `PostConsoleRunner` 다음에 접속 주소 출력 작업은 정상 실행된다.

## 오류 처리

이 프로젝트는 Spring Web 애플리케이션이므로 웹 서버 컨텍스트가 없으면 정상 실행 조건을 만족하지 못한 것으로 본다. 임의의 기본 포트로 대체하지 않고 애플리케이션 시작 실패를 그대로 드러내 잘못된 접속 주소가 출력되지 않게 한다.

## 검증

1. `.\gradlew.bat test`가 성공하는지 확인한다.
2. `.\gradlew.bat bootRun --args="--server.port=0"`으로 임시 포트에서 실행한다.
3. 게시글 전체 조회 결과 다음에 접속 주소가 출력되는지 확인한다.
4. 출력 주소의 포트가 실제 Tomcat 시작 포트와 같은지 확인한다.
5. `git diff --check`로 공백 오류를 확인한다.

## 제외 범위

- 브라우저 자동 실행
- 외부 IP 또는 배포 서버 주소 안내
- Actuator health 주소 추가 출력
- 기존 게시글 전체 조회 콘솔 출력 제거
