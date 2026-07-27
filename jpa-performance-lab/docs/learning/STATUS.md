# 현재 학습 상태

- 마지막 갱신일: 2026-07-27
- 현재 학습 단계: 5단계 — 게시글 수정과 삭제

## 완료된 작업

- `jpa-performance-lab` 프로젝트를 Java 26, Spring Boot 4.1.0, Gradle Groovy DSL로 생성했다.
- Spring Web MVC, JSP/JSTL, Spring Data JPA, PostgreSQL Driver, Validation, DevTools, Actuator 의존성을 구성했다.
- Supabase GDTCS 프로젝트에 `jpa_study` 스키마를 생성했다.
- `jpa_study.posts` 테이블과 테스트 데이터 3건을 생성하고 DBeaver에서 조회했다.
- 애플리케이션의 Actuator health에서 전체 상태와 `db` 구성요소가 `UP`인 것을 확인했다.
- 개인 저장소에서 각 PC의 추가 설정이 없게 하기 위해 DB 접속 계정과 비밀번호를 `application.yaml`에 직접 입력했다.
- 실행형 War와 JSP ViewResolver를 구성했다.
- `Post` 엔티티를 `jpa_study.posts` 테이블에 매핑하고 생성·수정 행위와 시간 생명주기를 추가했다.
- 게시글 기능을 Controller, Service 인터페이스, Service 구현체, Repository, Entity, DTO, Exception 계층으로 분리했다.
- `PostForm`에 제목·내용·작성자 검증 규칙을 추가했다.
- JSP 목록에서 전체 게시글을 조회하고 제목을 눌러 상세 화면으로 이동하는 동작을 확인했다.
- JSP 등록 폼, 취소 동작과 게시글 INSERT 후 상세 화면 리다이렉트를 확인했다.
- P6Spy로 바인딩 값이 적용된 INSERT와 SELECT를 Hibernate 형식으로 콘솔에 출력했다.
- Gradle 데몬, Java 컴파일, 테스트와 `bootRun`의 콘솔 인코딩을 UTF-8로 고정해 Git으로 공유했다.
- IntelliJ Community의 Gradle `bootRun`에서 게시글과 P6Spy SQL의 한글 출력을 확인했다.
- 시작 로그의 맨 끝에 실제 Tomcat 포트가 적용된 JSP 게시판 주소를 출력하도록 구성했다.

## 현재 확인된 실행 결과

- PostgreSQL 연결: 성공
- 대상 스키마: `jpa_study`
- Hibernate DDL 정책: `validate`
- Supavisor 연결: Shared Pooler Session Mode 5432
- HikariCP 최대 풀 크기: 5
- `PostMappingTest`: 성공
- `PostRepositoryTest`: 성공
- `PostTest`: 성공
- `/posts` 목록 조회: 성공
- `/posts/{id}` 상세 조회: 성공
- `/posts/new` 등록 화면과 취소: 성공
- `POST /posts` 등록과 상세 리다이렉트: 성공
- P6Spy 실제 바인딩 값 포함 SQL 포맷 출력: 성공
- IntelliJ Community Gradle `bootRun` 한글 출력: 성공
- 동적 포트 기반 게시판 접속 주소 출력: 성공
- `ApplicationUrlConsoleRunnerTest` 3개: 성공
- 전체 Gradle 테스트: 2026-07-27 17:26 `BUILD SUCCESSFUL`
- `bootWar`: 2026-07-27 16:19 `BUILD SUCCESSFUL`

## 다음에 할 한 단계

`PostService`와 `PostServiceImpl`에 게시글 수정 메서드를 추가하고 JPA 변경 감지로 UPDATE가 실행되는 흐름을 구현한다.

## 다음 단계 완료 조건

- 실습 게시글의 수정 폼에 기존 값이 표시된다.
- 수정 성공 후 상세 화면으로 리다이렉트된다.
- P6Spy 로그에서 실제 값이 포함된 UPDATE를 확인한다.
- 삭제는 사용자가 웹에서 새로 만든 실습 게시글에만 수행한다.
- 전체 Gradle 테스트가 성공한다.

## 알려진 문제 또는 막힌 점

- 개인 저장소에 DB 접속정보가 포함되어 있으므로 저장소를 공개하거나 공유하기 전 비밀번호 교체와 환경변수 전환이 필요하다.
- 빈 값 등록 시 화면 검증 메시지는 코드가 구현됐지만 브라우저에서 아직 명시적으로 확인하지 않았다.
- 등록과 SQL 로그 실습으로 생성된 게시글들은 수정·삭제 단계에서 정리해야 한다.
- P6Spy는 실행 시간과 데이터가 포함된 SQL을 출력하므로 성능 부하 테스트 전에 비활성화해야 한다.
- `PostConsoleRunner`는 학습 확인용 임시 코드이므로 전체 CRUD 확인 뒤 제거한다.
