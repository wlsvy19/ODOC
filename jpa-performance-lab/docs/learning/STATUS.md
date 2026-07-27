# 현재 학습 상태

- 마지막 갱신일: 2026-07-27
- 현재 학습 단계: 4단계 — 게시글 등록과 조회

## 완료된 작업

- `jpa-performance-lab` 프로젝트를 Java 26, Spring Boot 4.1.0, Gradle Groovy DSL로 생성했다.
- Spring Web MVC, Thymeleaf, Spring Data JPA, PostgreSQL Driver, Validation, DevTools, Actuator 의존성을 구성했다.
- Supabase GDTCS 프로젝트에 `jpa_study` 스키마를 생성했다.
- `jpa_study.posts` 테이블과 테스트 데이터 3건을 생성하고 DBeaver에서 조회했다.
- 애플리케이션의 Actuator health에서 전체 상태와 `db` 구성요소가 `UP`인 것을 확인했다.
- 개인 저장소에서 각 PC의 추가 설정이 없게 하기 위해 DB 접속 계정과 비밀번호를 `application.yaml`에 직접 입력했다.
- `Post` 엔티티를 `jpa_study.posts` 테이블에 매핑하고 Hibernate `validate`를 통과했다.
- `PostRepository.findAll()`로 기존 게시글을 전체 조회했다.
- `PostConsoleRunner`에서 Hibernate SELECT와 기존 게시글 3건을 IntelliJ 콘솔에 출력했다.
- IntelliJ VM 옵션으로 현재 PC의 콘솔 한글 출력을 확인했다.

## 현재 확인된 실행 결과

- PostgreSQL 연결: 성공
- 대상 스키마: `jpa_study`
- Hibernate DDL 정책: `validate`
- Supavisor 연결: Shared Pooler Session Mode 5432
- HikariCP 최대 풀 크기: 5
- `PostMappingTest`: 성공
- `PostRepositoryTest`: 성공
- `PostConsoleRunner` 전체 조회: 성공, 게시글 3건
- 전체 Gradle 테스트: 2026-07-27 `BUILD SUCCESSFUL`

## 다음에 할 한 단계

`application.yaml`의 `show-sql: true`와 `org.hibernate.SQL: debug` 중복을 정리해 SELECT가 한 번만 출력되게 하고 다시 실행한다.

## 다음 단계 완료 조건

- Hibernate SELECT가 콘솔에 한 번만 출력된다.
- 게시글 3건의 한글 필드가 깨지지 않고 출력된다.
- 전체 Gradle 테스트가 성공한다.

## 알려진 문제 또는 막힌 점

- 개인 저장소에 DB 접속정보가 포함되어 있으므로 저장소를 공개하거나 공유하기 전 비밀번호 교체와 환경변수 전환이 필요하다.
- `show-sql: true`와 `org.hibernate.SQL: debug`가 동시에 활성화돼 SELECT가 두 번 출력된다.
- IntelliJ UTF-8 VM 옵션이 Git 추적 파일로 확인되지 않아 다른 PC에서는 실행 설정을 다시 확인해야 한다.
- `PostConsoleRunner`는 학습 확인용 임시 코드이므로 웹 조회 단계에서 제거하거나 테스트 코드로 옮길지 결정해야 한다.
