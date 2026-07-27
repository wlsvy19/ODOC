# JPA Performance Lab

JPA 기본 CRUD부터 쿼리·인덱스 개선과 부하 테스트까지 단계적으로 학습하는 Spring Boot 게시판 프로젝트다. 사용자가 직접 코드를 작성하고 Codex 또는 Claude Code는 `docs/learning`의 상태를 읽어 한 단계씩 안내한다.

## 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 26 |
| Framework | Spring Boot 4.1.0 |
| Build | Gradle Groovy DSL, Wrapper 9.5.1 |
| Packaging | 실행형 War |
| Web | Spring Web MVC, JSP, JSTL |
| Persistence | Spring Data JPA, Hibernate |
| Database | Supabase PostgreSQL, `jpa_study` 스키마 |
| Observability | Spring Boot Actuator, P6Spy 학습용 SQL 로그 |
| Load Test | k6 |

## 새 PC에서 시작

PowerShell에서 ODoc 저장소로 이동한 뒤 로컬 변경을 먼저 확인한다.

```powershell
Set-Location C:\sw\ODOC
git status --short
git pull --ff-only
Set-Location .\jpa-performance-lab
```

미커밋 변경이 있으면 무조건 pull하지 말고 변경 출처와 충돌 가능성을 먼저 확인한다.

## 요구사항 확인

별도 Gradle 설치와 DB 환경변수 등록은 필요하지 않다.

```powershell
java -version
.\gradlew.bat --version
```

Java 26 toolchain과 Gradle 9.5.1을 사용한다.

## 데이터베이스 연결

애플리케이션은 다음 설정을 사용한다.

- Supabase PostgreSQL
- Shared Pooler Session Mode 5432
- SSL 필수
- `jpa_study` 스키마
- Hibernate `ddl-auto=validate`
- HikariCP 최대 풀 크기 5

개인 저장소에서 새 PC의 추가 설정을 없애기 위해 DB username과 password가 `application.yaml`에 포함돼 있다. 별도 IntelliJ 실행 설정이나 DB 환경변수 등록은 필요하지 않다.

실제 접속정보를 README, 이슈, 채팅, 커밋 메시지에 복사하지 않는다. 저장소를 공개하거나 공유하기 전에는 Supabase 비밀번호를 교체하고 환경변수 또는 비밀관리 도구로 전환한다.

## 테스트

```powershell
.\gradlew.bat test
```

`BUILD SUCCESSFUL`이 표시되어야 한다. 컨텍스트 테스트는 DB 연결과 Hibernate `validate` 설정도 확인한다.

## 애플리케이션 실행

```powershell
.\gradlew.bat bootRun
```

실행 후 다음 주소에서 상태를 확인한다.

- Health: `http://localhost:8080/actuator/health`
- JSP 게시판: `http://localhost:8080/posts`

전체 `status`와 `components.db.status`가 모두 `UP`이면 PostgreSQL 연결이 성공한 것이다.

실행형 War를 만들고 외부 Tomcat 없이 실행할 수도 있다.

```powershell
.\gradlew.bat bootWar
java -jar .\build\libs\jpa-performance-lab-0.0.1-SNAPSHOT.war
```

## SQL 로그

P6Spy를 사용해 PreparedStatement의 바인딩 값이 적용된 SQL을 Hibernate 형식으로 콘솔에 출력한다. 이 로그는 학습과 기능 확인에만 사용한다. JDBC 호출을 가로채고 SQL과 데이터가 출력되므로 성능 부하 테스트와 공개·운영 환경에서는 비활성화한다.

## 현재 학습 상태 확인

- 전체 순서: `docs/learning/ROADMAP.md`
- 현재 단계와 다음 한 단계: `docs/learning/STATUS.md`
- 기술 선택의 이유: `docs/learning/DECISIONS.md`

Codex 또는 Claude Code를 시작하면 위 문서를 읽고 다음 세 가지를 먼저 요약해야 한다.

1. 지금까지 완료된 것
2. 현재 학습 단계
3. 이번 세션에서 할 한 가지

현재 다음 단계는 `docs/learning/STATUS.md`의 `다음에 할 한 단계`를 기준으로 하며 이 README에 중복 기록하지 않는다.

## 학습 종료와 Git 인계

의미 있는 단계가 끝나면 테스트 결과를 확인하고 `STATUS.md`를 갱신한다. 단계 상태가 바뀌면 `ROADMAP.md`, 새로운 기술 결정이 있으면 `DECISIONS.md`도 함께 갱신한다.

다른 PC에서 이어갈 예정이면 Codex에 현재 진행 내용을 커밋하고 푸시하라고 명시적으로 요청한다.

```text
인계 저장
지금까지 진행한 것을 Git에 올려
커밋 후 push 진행해
```

이와 같은 요청은 현재 학습 상태 확인, 문서 갱신, 한글 커밋과 `origin/main` 푸시까지 승인한다. 푸시가 성공해야 다른 PC에서 이어받을 수 있다.

다른 PC에서는 다음 명령을 실행한다.

```powershell
Set-Location C:\sw\ODOC
git status --short
git pull --ff-only origin main
Set-Location .\jpa-performance-lab
```

Codex 작업 폴더를 `C:\sw\ODOC\jpa-performance-lab`으로 열고 다음과 같이 묻는다.

```text
어디까지 진행했어?
```

Codex는 완료된 것, 현재 단계, 다음 한 단계를 보고하고 사용자 직접 타이핑 방식으로 이어간다.

```powershell
Set-Location C:\sw\ODOC
git status --short
git diff
git add -p -- jpa-performance-lab
git diff --cached --check
git commit -m "JPA 학습 단계 진행 내용 기록"
git log origin/main..HEAD --format="%h %s"
```

커밋 제목과 본문은 한글로 작성한다. 푸시할 파일과 커밋 메시지를 확인한 뒤 사용자가 푸시를 요청한 경우에만 다음 명령을 실행한다.

```powershell
git push origin main
```

다른 PC에서는 원격 저장소에 푸시된 커밋만 이어받을 수 있다.

## 연결 방식 참고

- [Supabase 데이터베이스 연결 공식 문서](https://supabase.com/docs/guides/database/connecting-to-postgres)
