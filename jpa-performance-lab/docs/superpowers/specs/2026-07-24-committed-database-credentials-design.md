# 개인 저장소 DB 접속정보 하드코딩 변경 설계

## 1. 목적

`jpa-performance-lab`을 여러 개인 PC에서 사용할 때 저장소를 받은 직후 별도의 IntelliJ 실행 설정이나 운영체제 환경변수 등록 없이 애플리케이션을 실행한다.

이 저장소는 사용자만 접근하는 개인 저장소라는 전제 아래, 현재 Supabase PostgreSQL의 사용자명과 비밀번호를 `application.yaml`에 직접 기록하고 Git에 커밋한다.

## 2. 결정

다음 방식을 최종 선택한다.

- `spring.datasource.username`에 실제 DB 사용자명을 직접 기록한다.
- `spring.datasource.password`에 실제 DB 비밀번호를 직접 기록한다.
- `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD` 환경변수를 요구하지 않는다.
- `application.yaml`을 Git 추적 대상에서 제외하지 않는다.
- 새 PC에서는 저장소를 받은 뒤 Java 26만 준비하면 Gradle Wrapper로 바로 실행할 수 있게 한다.

이 결정은 기존의 환경변수 기반 접속정보 전달 정책을 대체한다.

## 3. 적용 범위

다음 파일을 새 정책에 맞게 변경한다.

- `src/main/resources/application.yaml`
- `docs/learning/STATUS.md`
- `docs/learning/DECISIONS.md`
- `AGENTS.md`
- `CLAUDE.md`
- `README.md`
- 기존 인계 설계와 구현 계획에서 환경변수 정책을 설명하는 부분

`Post` 엔티티, Repository, CRUD 구현은 이번 변경 범위에 포함하지 않는다.

## 4. 실행 흐름

새 PC에서의 실행 흐름은 다음과 같다.

1. `ODOC` 저장소를 받거나 최신 커밋을 가져온다.
2. `jpa-performance-lab` 디렉터리로 이동한다.
3. 별도 DB 환경변수 설정 없이 `.\gradlew.bat test` 또는 `.\gradlew.bat bootRun`을 실행한다.
4. Spring Boot가 `application.yaml`의 접속정보로 Supabase PostgreSQL에 연결한다.
5. Hibernate가 `jpa_study` 스키마를 `ddl-auto=validate` 정책으로 검증한다.

기존 Shared Pooler Session Mode 5432, SSL, `currentSchema=jpa_study`, HikariCP 최대 풀 크기 5 설정은 유지한다.

## 5. 문서와 AI 지침

문서에는 실제 사용자명과 비밀번호 값을 복사하지 않는다. 접속정보가 `application.yaml`에 포함돼 있어 별도 설정이 필요 없다는 사실만 기록한다.

Codex와 Claude Code는 다음 원칙을 따른다.

- `application.yaml`의 DB 접속정보 하드코딩은 사용자가 승인한 개인 저장소 예외로 취급한다.
- 사용자의 별도 요청 없이 환경변수 방식으로 되돌리지 않는다.
- 실제 비밀번호 값을 응답, 작업 보고서, 학습 문서, 커밋 메시지에 다시 출력하지 않는다.
- 자격 증명이 포함된 diff를 사용자 대화에 그대로 붙여 넣지 않는다.
- 저장소 공개 또는 공유 계획이 생기면 먼저 비밀번호 교체와 환경변수 전환을 안내한다.

## 6. 기존 설계와의 관계

이 문서는 `2026-07-24-multi-pc-ai-learning-handoff-design.md`의 다음 정책보다 우선한다.

- 비밀번호와 API 키 같은 비밀정보를 Git에 저장하지 않는다는 범위 제한
- README와 AI 지침에서 실제 DB 접속정보의 Git 저장을 금지한 규칙
- 환경변수 또는 제한 계정을 접속정보 전달 수단으로 사용한다는 보안 정책
- 어떤 문서나 Git 추적 파일에도 실제 비밀번호가 없어야 한다는 수용 조건

기존 인계 구조, 사용자 직접 실습, 한글 커밋 메시지, 사용자 요청 시에만 푸시하는 규칙은 그대로 유지한다.

## 7. 오류 처리

- 하드코딩한 접속정보가 잘못되어 테스트가 실패하면 비밀번호를 화면에 출력하지 않고 인증 실패 사실만 보고한다.
- 환경변수를 대체 경로로 사용하지 않는다. `application.yaml`을 단일 접속정보 원본으로 사용한다.
- 새 PC에서 연결에 실패하면 Java 버전, 네트워크, Supabase 프로젝트 상태, 접속정보 변경 여부를 순서대로 확인한다.
- 저장소 접근 범위가 개인 전용이 아니게 되면 현재 비밀번호를 먼저 교체하고 Git 이력 노출 여부를 점검한다.

## 8. 검증

변경 후 다음 사항을 확인한다.

1. `application.yaml`의 username과 password가 환경변수 참조가 아니다.
2. 현재 프로세스에서 `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD`를 제거한 상태로 `.\gradlew.bat test`가 성공한다.
3. 테스트가 Java 26 toolchain을 사용한다.
4. `STATUS.md`, `DECISIONS.md`, `AGENTS.md`, `CLAUDE.md`, `README.md`에 환경변수 필수 안내가 남아 있지 않다.
5. 문서, 작업 보고서, 커밋 메시지에는 실제 비밀번호 값이 나타나지 않는다.
6. 새 커밋 메시지는 한글로 작성한다.
7. 사용자가 별도로 요청하기 전에는 푸시하지 않는다.

## 9. 수용 조건

- 새 PC에서 저장소를 받은 뒤 DB 관련 추가 설정 없이 테스트와 애플리케이션 실행이 가능하다.
- Supabase 연결, `jpa_study` 스키마, Hibernate `validate`, HikariCP 최대 크기 5 설정이 유지된다.
- AI가 승인된 하드코딩 정책을 임의로 환경변수 방식으로 변경하지 않는다.
- 접속정보가 Git에 포함된다는 위험을 사용자가 명시적으로 수용한 결정이 `DECISIONS.md`에 남는다.
- 저장소 공개·공유 시 비밀번호 교체와 정책 전환 조건이 문서화된다.
