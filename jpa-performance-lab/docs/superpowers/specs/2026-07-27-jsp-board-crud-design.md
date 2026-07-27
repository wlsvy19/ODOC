# JSP 게시판 CRUD 설계

- 작성일: 2026-07-27
- 대상 프로젝트: `jpa-performance-lab`
- 상태: 사용자 승인 완료

## 1. 목표

현재 `jpa_study.posts` 테이블과 `Post` 엔티티를 사용해 JSP 기반 게시판 CRUD를 만든다. 사용자는 Java, JSP와 테스트 코드를 한 파일씩 직접 타이핑하며 Spring MVC, JSP/JSTL, Service 트랜잭션과 JPA 변경 감지를 학습한다.

첫 번째 완료 범위는 다음과 같다.

- 게시글 목록 조회
- 게시글 상세 조회
- 게시글 등록
- 게시글 수정
- 게시글 삭제
- 입력값 검증
- 기본적인 404 처리
- Service와 Controller 테스트

## 2. 제외 범위

첫 번째 CRUD에서는 다음 기능을 구현하지 않는다.

- 로그인과 작성자 인증
- 댓글과 첨부파일
- 검색과 페이징
- REST API와 JavaScript 비동기 통신
- Bootstrap 등 CSS 프레임워크
- 사용자 정의 500 오류 화면
- 부하 테스트

검색, 페이징과 부하 테스트는 기본 CRUD를 완료하고 동작 기준을 확보한 뒤 진행한다.

## 3. 현재 프로젝트와 전환 이유

현재 프로젝트는 Spring Boot 4.1.0, Java 26, Gradle, 실행형 Jar와 Thymeleaf로 구성되어 있다. JSP 자체를 학습하기로 결정했으므로 Thymeleaf를 제거하고 실행형 War로 전환한다.

Spring Boot 4.1은 실행형 Jar에서 JSP를 지원하지 않는다. Tomcat과 JSP를 사용할 때는 War 패키징을 사용해야 하며, 실행형 War는 내장 Tomcat으로 `main()`과 `bootRun` 또는 `java -jar` 실행이 가능하다.

참고 문서:

- [Spring Boot JSP 제약](https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
- [Spring Boot War 배포 구성](https://docs.spring.io/spring-boot/how-to/deployment/traditional-deployment.html)
- [Spring MVC JSP와 JSTL](https://docs.spring.io/spring-framework/reference/web/webmvc-view/mvc-jsp.html)

## 4. 실행환경 설계

### 4.1 Gradle

`build.gradle`은 다음 원칙으로 변경한다.

- Gradle `war` 플러그인을 적용한다.
- Thymeleaf 구현 및 테스트 의존성을 제거한다.
- JSP 컴파일을 위한 `org.apache.tomcat.embed:tomcat-embed-jasper`를 추가한다.
- JSTL API인 `jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api`를 추가한다.
- JSTL 구현체인 `org.glassfish.web:jakarta.servlet.jsp.jstl`을 추가한다.
- 내장 Tomcat 런타임은 `providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat-runtime'`으로 구성한다.
- 각 의존성 버전은 Spring Boot 4.1.0의 의존성 관리를 따른다.

### 4.2 애플리케이션 진입점

`JpaPerformanceLabApplication`은 `SpringBootServletInitializer`를 상속하고 `configure()`에서 현재 애플리케이션 소스를 지정한다. 기존 `main()`은 유지해 IntelliJ Community와 `bootRun`에서 계속 실행할 수 있게 한다.

지원할 실행 방법은 다음과 같다.

```powershell
.\gradlew.bat bootRun
.\gradlew.bat bootWar
java -jar build\libs\jpa-performance-lab-0.0.1-SNAPSHOT.war
```

외부 Tomcat 설치는 요구하지 않는다.

### 4.3 JSP와 정적 파일

JSP는 클라이언트가 직접 접근하지 못하도록 `WEB-INF` 아래에 둔다.

```text
src/main/webapp/WEB-INF/jsp/posts/list.jsp
src/main/webapp/WEB-INF/jsp/posts/detail.jsp
src/main/webapp/WEB-INF/jsp/posts/form.jsp
```

CSS는 다음 위치에 둔다.

```text
src/main/resources/static/css/board.css
```

JSP ViewResolver의 prefix는 `/WEB-INF/jsp/`, suffix는 `.jsp`로 구성한다. 기존 DB 설정을 유지한 채 `application.yaml`의 `spring.mvc.view.prefix`와 `spring.mvc.view.suffix`에 추가한다.

## 5. 애플리케이션 구조

```text
JSP
  ↓
PostController
  ↓
PostService
  ↓
PostRepository
  ↓
Supabase jpa_study.posts
```

### 5.1 `PostController`

- URL과 HTTP 요청을 처리한다.
- JSP에 필요한 모델을 전달한다.
- `PostForm` 검증 결과를 확인한다.
- 성공한 쓰기 요청은 리다이렉트한다.
- 비즈니스 트랜잭션이나 엔티티 수정 로직을 포함하지 않는다.

### 5.2 `PostService`

- 목록, 상세, 등록, 수정과 삭제 사용 사례를 제공한다.
- 조회 메서드는 읽기 전용 트랜잭션을 사용한다.
- 등록, 수정과 삭제 메서드는 쓰기 트랜잭션을 사용한다.
- 게시글이 없으면 `PostNotFoundException`을 발생시킨다.

### 5.3 `PostRepository`

기존 `JpaRepository<Post, Long>`를 유지한다. 첫 목록은 별도 쿼리 메서드를 추가하지 않고 `Sort`를 전달해 `id` 내림차순으로 조회한다.

### 5.4 `PostForm`

등록과 수정 화면에서 공통으로 사용하는 입력 전용 객체다.

- `title`: 필수, 최대 200자
- `author`: 필수, 최대 50자
- `content`: 필수

엔티티를 요청 파라미터에 직접 바인딩하지 않는다. 수정 화면을 열 때는 기존 `Post` 값으로 `PostForm`을 만든다.

### 5.5 `Post`

기존 테이블 매핑을 유지하면서 다음 행위를 추가한다.

- 새 게시글 생성
- 제목, 작성자와 내용 수정
- `@PrePersist`에서 `OffsetDateTime.now(ZoneOffset.UTC)`로 `createdAt`과 `updatedAt` 최초 설정
- `@PreUpdate`에서 같은 UTC 기준으로 `updatedAt` 갱신

수정은 Service 트랜잭션 안에서 관리 상태 엔티티의 `update()`를 호출한다. JPA 변경 감지를 학습하기 위해 수정 후 `save()`를 다시 호출하지 않는다.

### 5.6 `PostConsoleRunner`

JSP 목록에서 기존 게시글 3건이 정상 출력되는 것을 확인한 뒤 제거한다. 웹 조회와 콘솔 조회가 애플리케이션 시작 시 함께 실행되는 상태를 유지하지 않는다.

## 6. 화면과 URL

선택된 화면은 전통적인 표 형태의 게시판이다.

| HTTP | URL | 처리 | 결과 |
|---|---|---|---|
| GET | `/posts` | 게시글 목록 조회 | `posts/list` |
| GET | `/posts/{id}` | 게시글 상세 조회 | `posts/detail` |
| GET | `/posts/new` | 등록 폼 표시 | `posts/form` |
| POST | `/posts` | 게시글 등록 | 새 게시글 상세로 리다이렉트 |
| GET | `/posts/{id}/edit` | 수정 폼 표시 | `posts/form` |
| POST | `/posts/{id}/edit` | 게시글 수정 | 같은 게시글 상세로 리다이렉트 |
| POST | `/posts/{id}/delete` | 게시글 삭제 | 목록으로 리다이렉트 |

HTML 폼이 기본으로 지원하는 GET과 POST만 사용한다. PUT과 DELETE 변환 필터는 이번 범위에 추가하지 않는다.

### 6.1 목록

- 번호, 제목, 작성자와 작성일을 표로 표시한다.
- `id`가 큰 게시글부터 출력한다.
- 제목은 상세 화면 링크다.
- 상단에 글쓰기 버튼을 둔다.

### 6.2 상세

- 제목, 작성자, 작성일, 수정일과 내용을 표시한다.
- 목록, 수정과 삭제 버튼을 둔다.
- 삭제는 POST 폼으로 전송한다.
- 삭제 전 브라우저 기본 확인창을 한 번 표시한다.

### 6.3 등록과 수정

등록과 수정은 `form.jsp` 하나를 재사용한다. Controller가 화면 제목, POST 대상 URL과 버튼 문구를 모델로 전달해 JSP가 등록과 수정 엔드포인트를 임의로 판단하지 않게 한다.

## 7. 요청과 데이터 흐름

### 7.1 목록과 상세

```text
GET 요청
→ Controller
→ 읽기 전용 Service
→ Repository
→ Model
→ JSP
```

### 7.2 등록

```text
POST /posts
→ PostForm 검증
→ Post 생성
→ Repository.save()
→ 새 ID를 포함한 상세 URL로 redirect
```

### 7.3 수정

```text
POST /posts/{id}/edit
→ PostForm 검증
→ Post 조회
→ Post.update()
→ 트랜잭션 종료 시 변경 감지 UPDATE
→ 상세 URL로 redirect
```

### 7.4 삭제

```text
POST /posts/{id}/delete
→ Post 조회
→ Repository.delete()
→ 목록 URL로 redirect
```

성공한 쓰기 요청은 PRG(Post/Redirect/Get) 방식으로 처리해 새로고침으로 같은 쓰기 요청이 반복되지 않게 한다.

## 8. JSP 출력과 검증

- 목록은 JSTL `<c:forEach>`로 반복 출력한다.
- 사용자 데이터는 `<c:out>`으로 출력해 HTML을 그대로 실행하지 않게 한다.
- 입력 폼은 Spring `<form:form>`으로 `PostForm`에 바인딩한다.
- 필드 오류는 `<form:errors>`로 해당 입력 아래에 표시한다.
- 검증 실패 시 리다이렉트하지 않고 같은 폼을 반환해 입력값과 오류 메시지를 보존한다.
- 화면 스타일은 직접 작성한 `board.css`만 사용한다.

## 9. 오류 처리

- 존재하지 않는 ID를 조회, 수정 또는 삭제하면 `PostNotFoundException`을 발생시킨다.
- `PostNotFoundException`은 `RuntimeException`을 상속하고 `@ResponseStatus(HttpStatus.NOT_FOUND)`를 사용해 HTTP 404로 응답한다.
- 검증 오류는 HTTP 요청을 처리한 폼 화면에서 사용자에게 표시한다.
- 예상하지 못한 DB 또는 서버 오류는 이번 범위에서 Spring Boot 기본 오류 처리에 맡긴다.
- 로그인 기능이 없으므로 누구나 등록, 수정과 삭제할 수 있다는 점을 학습용 제약으로 명시한다.

## 10. 테스트와 수동 검증

### 10.1 기존 테스트

- `PostMappingTest`를 유지한다.
- `PostRepositoryTest`를 유지한다.

### 10.2 Service 통합 테스트

- 등록 성공
- 상세 조회 성공
- 수정 시 변경 감지 성공
- 삭제 성공
- 존재하지 않는 ID 조회 시 404 대상 예외 발생

Supabase를 사용하는 쓰기 테스트는 `@Transactional`로 롤백한다. PostgreSQL 시퀀스 값은 롤백되지 않을 수 있으므로 테스트는 ID 연속성을 가정하지 않는다.

### 10.3 Controller 테스트

- 목록 view 이름과 모델 확인
- 상세 view 이름과 모델 확인
- 등록과 수정 검증 실패 시 폼 재표시
- 등록, 수정과 삭제 성공 시 리다이렉트 확인
- Service는 Mock으로 대체해 Controller 책임만 검증

### 10.4 브라우저 검증

1. 기존 게시글 3건이 목록에 한글로 표시되는지 확인한다.
2. 기존 게시글의 상세 화면을 확인한다.
3. 별도의 실습 게시글을 등록한다.
4. 등록한 게시글을 수정한다.
5. 수정한 게시글을 삭제한다.
6. 기존 게시글 3건이 유지되는지 확인한다.

### 10.5 빌드 검증

```powershell
.\gradlew.bat test
.\gradlew.bat bootWar
```

두 명령이 모두 성공해야 JSP 게시판 CRUD 단계를 완료로 표시한다.

## 11. 구현 순서

사용자가 한 번에 한 파일 또는 한 단계씩 직접 타이핑하고 각 단계의 실행 결과를 확인한 뒤 다음 단계로 넘어간다.

1. Gradle War/JSP 의존성 전환
2. 애플리케이션 War 진입점 구성
3. JSP ViewResolver 구성과 빈 JSP 렌더링 확인
4. 목록 조회와 `list.jsp`
5. 상세 조회와 `detail.jsp`
6. `PostForm`과 등록
7. 엔티티 변경 감지와 수정
8. 삭제
9. 입력 검증과 404 처리
10. Service와 Controller 테스트
11. `PostConsoleRunner` 제거
12. 전체 테스트와 `bootWar` 검증

## 12. 완료 조건

- IntelliJ Community의 `main()` 또는 Gradle `bootRun`으로 실행된다.
- `/posts`에서 기존 게시글 3건이 표 형태로 표시된다.
- 상세, 등록, 수정과 삭제가 JSP 화면에서 동작한다.
- 입력 오류가 해당 폼에 표시되고 입력값이 유지된다.
- 없는 게시글은 HTTP 404를 반환한다.
- 사용자 입력이 JSP에서 HTML로 실행되지 않는다.
- 등록, 수정과 삭제 후 PRG 방식으로 이동한다.
- 기존 테스트와 새 Service/Controller 테스트가 성공한다.
- `bootWar`가 실행 가능한 War 파일을 만든다.
- 기존 테스트 데이터 3건이 보존된다.
