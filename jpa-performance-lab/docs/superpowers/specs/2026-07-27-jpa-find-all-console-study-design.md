# JPA 전체 조회와 콘솔 출력 학습 설계

- 작성일: 2026-07-27
- 대상 프로젝트: `jpa-performance-lab`
- 대상 테이블: `jpa_study.posts`

## 목적

사용자가 JPA의 엔티티 매핑과 Spring Data JPA의 기본 전체 조회 흐름을 직접 타이핑하며 학습한다. 애플리케이션 시작 시 `PostRepository.findAll()`을 한 번 실행하고, Supabase PostgreSQL에서 조회된 게시글을 IntelliJ 실행 콘솔에서 확인한다.

## 현재 상태

- Supabase PostgreSQL 연결과 Actuator DB 상태 확인이 완료됐다.
- `jpa_study.posts` 테이블과 테스트 데이터 3건이 존재한다.
- Hibernate는 `ddl-auto=validate`와 `jpa_study` 기본 스키마를 사용한다.
- `Post` 엔티티와 Repository는 아직 없다.

## 학습 진행 원칙

- 모든 Java 실습 코드는 사용자가 직접 타이핑한다.
- 사용자가 명시적으로 구현을 요청하지 않는 한 AI는 Java 소스 파일을 생성하거나 수정하지 않는다.
- AI는 한 번에 한 파일 또는 한 단계의 코드만 안내한다.
- 각 단계에서 코드의 역할, 실행 방법, 완료 조건을 설명한다.
- 사용자가 실행 결과나 오류를 전달하면 이를 확인한 뒤 다음 단계로 넘어간다.
- 이 원칙은 `AGENTS.md`에 작업 규칙으로, `docs/learning/DECISIONS.md`에 기술 결정으로 기록한다.

## 구성 요소

### `Post` 엔티티

- 패키지: `com.study.jpalab.post`
- 테이블: `jpa_study.posts`
- `id`: `Long`, `GenerationType.IDENTITY`
- `title`, `content`, `author`: `String`
- `created_at`, `updated_at`: `OffsetDateTime`
- 스네이크 케이스 시간 컬럼은 `@Column`으로 명시한다.
- JPA가 사용할 기본 생성자를 제공한다.

### `PostRepository`

- 패키지: `com.study.jpalab.post`
- `JpaRepository<Post, Long>`을 상속한다.
- 이번 단계에서는 별도 쿼리 메서드나 Native Query를 만들지 않는다.
- 전체 조회는 상속받은 `findAll()`을 사용한다.

### `PostConsoleRunner`

- 패키지: `com.study.jpalab.post`
- 별도 클래스로 만들고 `CommandLineRunner`를 구현한다.
- 생성자 주입으로 `PostRepository`를 받는다.
- 애플리케이션 시작 시 `findAll()`을 한 번 호출한다.
- 조회된 각 게시글의 `id`, `title`, `content`, `author`, `createdAt`, `updatedAt`을 콘솔에 출력한다.

## 실행 흐름

1. Spring Boot 애플리케이션을 실행한다.
2. Hibernate가 `Post` 엔티티와 `jpa_study.posts` 테이블 구조를 검증한다.
3. Spring Data JPA가 `PostRepository` 구현체를 생성한다.
4. `PostConsoleRunner`가 `postRepository.findAll()`을 호출한다.
5. Hibernate가 전체 게시글 조회 SQL을 실행한다.
6. 조회 결과가 `Post` 객체 목록으로 변환된다.
7. 게시글 3건의 필드 값이 IntelliJ 실행 콘솔에 출력된다.

JPA가 생성하는 SQL은 `select * from posts`와 의미는 같지만 실제 로그에서는 매핑된 컬럼 이름을 명시할 수 있다.

## SQL 로그

학습 중 실행된 SELECT 문을 확인할 수 있도록 Hibernate SQL 로깅을 활성화한다. 바인딩 값 상세 로그는 이번 조회에 필요하지 않으므로 활성화하지 않는다. DB 사용자명과 비밀번호는 어떤 로그에도 출력하지 않는다.

## 오류 처리

- DB 연결 실패, 스키마 불일치, 엔티티 매핑 오류를 임의로 잡아서 숨기지 않는다.
- Hibernate `validate` 오류가 발생하면 테이블과 엔티티의 타입·컬럼명을 비교한다.
- 조회 결과가 없으면 빈 목록임을 콘솔에 명확히 표시한다.
- 오류가 발생하면 사용자가 콘솔의 핵심 오류를 전달하고 AI가 해당 단계만 진단한다.

## 검증

1. `.\gradlew.bat test`가 성공한다.
2. IntelliJ에서 애플리케이션을 실행하거나 `.\gradlew.bat bootRun`을 실행한다.
3. 콘솔에서 Hibernate의 전체 조회 SELECT를 확인한다.
4. 기존 테스트 데이터 3건의 필드 값을 확인한다.
5. Native Query 없이 `findAll()`만 사용했는지 확인한다.

## 이번 범위에서 제외

- 게시글 등록, 단건 조회, 수정, 삭제
- 웹 컨트롤러와 Thymeleaf 화면
- Native Query와 JPQL
- 페이징, 검색, 성능 측정
- `PostConsoleRunner`를 운영 기능으로 유지하는 결정

`PostConsoleRunner`는 학습 확인용 임시 진입점이다. 웹 조회 단계에 도달하면 제거하거나 테스트 코드로 옮길지 다시 결정한다.

## 문서와 Git

- 구현 전에 `AGENTS.md`와 `DECISIONS.md`에 사용자 직접 타이핑 원칙을 반영한다.
- 각 단계가 검증되면 `STATUS.md`와 필요 시 `ROADMAP.md`를 갱신한다.
- AI가 만드는 커밋 메시지는 한글로 작성한다.
- 사용자가 명시적으로 요청하기 전에는 원격 저장소에 푸시하지 않는다.
