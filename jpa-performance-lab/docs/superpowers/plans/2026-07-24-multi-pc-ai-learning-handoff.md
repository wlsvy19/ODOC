# 여러 PC·AI JPA 학습 인계 구현 계획

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** `ODOC/jpa-performance-lab`을 어느 PC에서 받아도 Codex 또는 Claude Code가 동일한 학습 상태를 읽고, 사용자가 JPA 실습을 한 단계씩 이어갈 수 있게 한다.

**Architecture:** Git에 커밋된 `docs/learning` 문서를 학습 상태의 단일 기준으로 삼고, `AGENTS.md`와 `CLAUDE.md`는 이 공통 문서를 읽게 하는 얇은 진입점으로 둔다. `README.md`는 사람과 AI가 공유하는 실행 안내서로 사용하며, 데이터베이스 비밀번호는 Git이 아니라 운영체제 사용자 환경변수로 전달한다.

**Tech Stack:** Java 26, Spring Boot 4.1.0, Gradle Groovy DSL, Gradle Wrapper 9.5.1, Spring Data JPA, Thymeleaf, PostgreSQL, Supabase Shared Pooler Session Mode, Markdown, PowerShell

## Global Constraints

- Git 루트는 `C:\sw\ODOC`이고 `jpa-performance-lab`은 이 모노레포 안의 하위 프로젝트로 유지한다.
- 프로젝트명과 Gradle 루트 프로젝트명은 `jpa-performance-lab`, 기본 패키지는 `com.study.jpalab`을 사용한다.
- Java 26, Spring Boot 4.1.0, Gradle Groovy DSL, Gradle Wrapper 9.5.1을 유지한다.
- Supabase GDTCS 프로젝트의 `jpa_study` 스키마와 기존 `posts` 테이블을 사용한다.
- JDBC는 Shared Pooler Session Mode의 5432 포트와 SSL을 사용한다.
- Hibernate는 `ddl-auto=validate`, `hibernate.default_schema=jpa_study`를 유지한다.
- HikariCP 초기 최대 풀 크기는 5를 유지한다.
- 실제 데이터베이스 비밀번호, API 키, 토큰은 Git 추적 파일·학습 문서·커밋 메시지에 기록하지 않는다.
- 사용자가 직접 실습하므로 이번 계획에서는 `Post` 엔티티, Repository, CRUD 코드를 구현하지 않는다.
- AI는 한 번에 한 단계만 안내하고 사용자의 실행 결과를 확인한 뒤 다음 단계로 넘어간다.
- AI가 만드는 Git 커밋 메시지의 제목과 본문은 한글로 작성한다. 기술 식별자와 명령어는 필요한 경우 원문을 유지할 수 있다.
- 푸시 전에 푸시 범위와 커밋 메시지를 확인하고, 사용자가 명시적으로 요청한 경우에만 푸시한다.
- 기존 사용자 변경을 덮어쓰거나 임의로 정리하지 않는다. 각 작업에서는 계획에 적힌 경로만 스테이징한다.
- 기준 설계는 `docs/superpowers/specs/2026-07-24-multi-pc-ai-learning-handoff-design.md`이다.

---

## 파일 구조와 책임

| 경로 | 작업 | 책임 |
|---|---|---|
| `jpa-performance-lab/src/main/resources/application.yaml` | 수정 | 비밀번호를 Git 밖의 환경변수에서 받고 기존 Supabase/JPA 설정을 유지한다. |
| `jpa-performance-lab/docs/learning/ROADMAP.md` | 생성 | 쉽게 바뀌지 않는 전체 JPA 학습 순서와 단계별 상태를 관리한다. |
| `jpa-performance-lab/docs/learning/STATUS.md` | 생성 | 새 세션이 바로 이어서 작업하는 데 필요한 현재 상태만 관리한다. |
| `jpa-performance-lab/docs/learning/DECISIONS.md` | 생성 | 반복 논의를 막기 위한 기술 선택과 변경 조건을 기록한다. |
| `jpa-performance-lab/AGENTS.md` | 생성 | Codex가 공통 학습 문서를 읽고 작업하게 하는 프로젝트 지침이다. |
| `jpa-performance-lab/CLAUDE.md` | 생성 | Claude Code가 Codex와 같은 공통 학습 상태를 읽게 하는 진입점이다. |
| `jpa-performance-lab/README.md` | 생성 | 새 PC 준비, 실행, 테스트, 상태 확인, Git 인계 절차를 설명한다. |

### Task 1: 프로젝트 초기 구성을 안전하게 Git 기준선으로 확정

**Files:**
- Delete as already intended: `jpa-board-study/.gitattributes`
- Delete as already intended: `jpa-board-study/.gitignore`
- Delete as already intended: `jpa-board-study/build.gradle`
- Delete as already intended: `jpa-board-study/gradle/wrapper/gradle-wrapper.jar`
- Delete as already intended: `jpa-board-study/gradle/wrapper/gradle-wrapper.properties`
- Delete as already intended: `jpa-board-study/gradlew`
- Delete as already intended: `jpa-board-study/gradlew.bat`
- Delete as already intended: `jpa-board-study/settings.gradle`
- Delete as already intended: `jpa-board-study/src/main/java/com/study/jpaboard/JpaBoardStudyApplication.java`
- Delete as already intended: `jpa-board-study/src/main/resources/application.properties`
- Delete as already intended: `jpa-board-study/src/test/java/com/study/jpaboard/JpaBoardStudyApplicationTests.java`
- Add existing project skeleton: `jpa-performance-lab/.gitattributes`
- Add existing project skeleton: `jpa-performance-lab/.gitignore`
- Add existing project skeleton: `jpa-performance-lab/build.gradle`
- Add existing project skeleton: `jpa-performance-lab/gradle/wrapper/gradle-wrapper.jar`
- Add existing project skeleton: `jpa-performance-lab/gradle/wrapper/gradle-wrapper.properties`
- Add existing project skeleton: `jpa-performance-lab/gradlew`
- Add existing project skeleton: `jpa-performance-lab/gradlew.bat`
- Add existing project skeleton: `jpa-performance-lab/settings.gradle`
- Add existing project skeleton: `jpa-performance-lab/src/main/java/com/study/jpalab/JpaPerformanceLabApplication.java`
- Modify and add: `jpa-performance-lab/src/main/resources/application.yaml`
- Add existing project skeleton: `jpa-performance-lab/src/test/java/com/study/jpalab/JpaPerformanceLabApplicationTests.java`

**Interfaces:**
- Consumes: 사용자가 이미 만든 Spring Initializr 프로젝트와 검증된 Supabase 연결 설정
- Produces: 다른 PC에서 Gradle로 빌드할 수 있는 `jpa-performance-lab` 기준선과 `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD` 환경변수 계약

- [ ] **Step 1: 현재 변경 범위가 계획과 같은지 확인**

Run:

```powershell
Set-Location C:\sw\ODOC
git status --short -- jpa-board-study jpa-performance-lab
git diff -- jpa-board-study
```

Expected:

- `jpa-board-study`의 기존 Spring Initializr 파일은 삭제 상태다.
- `jpa-performance-lab`의 Spring Initializr 파일은 추가 대상이다.
- `docs/superpowers` 아래 이미 커밋된 설계와 계획 외에 출처를 알 수 없는 변경이 없어야 한다.
- 계획에 없는 변경이 있으면 스테이징하지 말고 사용자에게 한 가지 질문으로 출처를 확인한다.

- [ ] **Step 2: 현재 PC에 DB 접속정보를 사용자 환경변수로 안전하게 저장**

Run:

```powershell
$dbCredential = Get-Credential -Message 'jpa_study 전용 데이터베이스 계정 입력'
[Environment]::SetEnvironmentVariable('JPA_STUDY_DB_USERNAME', $dbCredential.UserName, 'User')
[Environment]::SetEnvironmentVariable('JPA_STUDY_DB_PASSWORD', $dbCredential.GetNetworkCredential().Password, 'User')
$env:JPA_STUDY_DB_USERNAME = $dbCredential.UserName
$env:JPA_STUDY_DB_PASSWORD = $dbCredential.GetNetworkCredential().Password
Remove-Variable dbCredential
```

Expected:

- 자격 증명 입력 창에서 사용자가 직접 계정과 비밀번호를 입력한다.
- 비밀번호가 명령어, 계획 문서, 터미널 출력에 평문으로 나타나지 않는다.
- 새 PC에서는 이 절차를 한 번 수행한 뒤 IntelliJ와 터미널을 다시 시작하면 별도의 IntelliJ 실행 설정 없이 사용할 수 있다.

- [ ] **Step 3: YAML의 접속 계정과 비밀번호를 환경변수 참조로 변경**

Modify `jpa-performance-lab/src/main/resources/application.yaml`의 datasource 자격 증명 두 줄을 다음과 같이 만든다. 나머지 URL, HikariCP, JPA, Actuator 설정은 변경하지 않는다.

```yaml
  datasource:
    url: "jdbc:postgresql://aws-1-ap-northeast-2.pooler.supabase.com:5432/postgres?sslmode=require&currentSchema=jpa_study"
    username: "${JPA_STUDY_DB_USERNAME}"
    password: "${JPA_STUDY_DB_PASSWORD}"
```

- [ ] **Step 4: 비밀값이 제거되고 설정 계약이 정확한지 정적 검증**

Run:

```powershell
$yamlPath = 'jpa-performance-lab/src/main/resources/application.yaml'
$yamlText = Get-Content -Raw -Encoding UTF8 $yamlPath
if ($yamlText -notmatch 'username:\s*"\$\{JPA_STUDY_DB_USERNAME\}"') { throw 'DB username 환경변수 참조가 없습니다.' }
if ($yamlText -notmatch 'password:\s*"\$\{JPA_STUDY_DB_PASSWORD\}"') { throw 'DB password 환경변수 참조가 없습니다.' }
if ($yamlText -notmatch 'currentSchema=jpa_study') { throw 'currentSchema 설정이 없습니다.' }
if ($yamlText -notmatch 'default_schema:\s*jpa_study') { throw 'Hibernate default_schema 설정이 없습니다.' }
if ($yamlText -notmatch 'maximum-pool-size:\s*5') { throw 'Hikari 최대 풀 크기가 5가 아닙니다.' }
'application.yaml static check=PASS'
```

Expected:

```text
application.yaml static check=PASS
```

- [ ] **Step 5: Gradle, Java, Spring 컨텍스트와 DB 연결 검증**

Run:

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
.\gradlew.bat --version
.\gradlew.bat test
```

Expected:

- Gradle 버전은 `9.5.1`이다.
- JVM은 Java 26이다.
- 테스트는 `BUILD SUCCESSFUL`로 끝난다.
- `JpaPerformanceLabApplicationTests.contextLoads()`가 Supabase PostgreSQL에 연결하고 Hibernate `validate`를 통과한다.

- [ ] **Step 6: 프로젝트 교체 범위를 정확히 스테이징하고 검토**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -A -- jpa-board-study jpa-performance-lab
git diff --cached --check
git diff --cached --name-status
git diff --cached -- jpa-performance-lab/src/main/resources/application.yaml
```

Expected:

- 기존 `jpa-board-study` 삭제와 새 `jpa-performance-lab` 초기 구성이 함께 보인다.
- `application.yaml`에는 환경변수 이름만 있고 실제 비밀번호는 없다.
- `HELP.md`, `.idea`, `.gradle`, `build`는 스테이징되지 않는다.
- 설계·계획 파일에 추가 변경이 없다면 이 단계에서 다시 스테이징되지 않는다.

- [ ] **Step 7: 한글 커밋 메시지로 프로젝트 기준선 커밋**

Run:

```powershell
git commit -m "JPA 성능 실험 프로젝트 초기 구성"
```

Expected: 새 커밋 한 개가 생성되고 원격 저장소에는 아직 푸시되지 않는다.

### Task 2: 공통 학습 상태 문서 생성

**Files:**
- Create: `jpa-performance-lab/docs/learning/ROADMAP.md`
- Create: `jpa-performance-lab/docs/learning/STATUS.md`
- Create: `jpa-performance-lab/docs/learning/DECISIONS.md`

**Interfaces:**
- Consumes: Task 1의 실행 가능한 프로젝트 기준선과 현재까지 확인된 DB 연결 결과
- Produces: `AGENTS.md`, `CLAUDE.md`, `README.md`가 참조할 학습 상태의 단일 기준

- [ ] **Step 1: ROADMAP 문서 작성**

Create `jpa-performance-lab/docs/learning/ROADMAP.md`:

```markdown
# JPA 학습 로드맵

이 문서는 전체 학습 순서만 관리한다. 구체적인 현재 작업은 `STATUS.md`, 기술 선택의 이유는 `DECISIONS.md`에서 관리한다.

| 단계 | 상태 | 학습 결과 |
|---:|---|---|
| 1 | 완료 | 프로젝트 생성 및 Supabase PostgreSQL 연결 |
| 2 | 진행 중 | `Post` 엔티티와 `jpa_study.posts` 테이블 매핑 |
| 3 | 대기 | Spring Data JPA Repository 작성 |
| 4 | 대기 | 게시글 등록과 조회 |
| 5 | 대기 | 게시글 수정과 삭제 |
| 6 | 대기 | Thymeleaf 게시판 화면 |
| 7 | 대기 | JPA 테스트 |
| 8 | 대기 | 페이징과 검색 |
| 9 | 대기 | Actuator 지표 관측 |
| 10 | 대기 | k6 Smoke·Baseline 부하 테스트 |
| 11 | 대기 | 쿼리 및 인덱스 개선 전후 비교 |

상태는 `대기`, `진행 중`, `완료` 중 하나만 사용한다. 단계가 끝나면 이 표와 `STATUS.md`를 같은 커밋에서 갱신한다.
```

- [ ] **Step 2: STATUS 문서 작성**

Create `jpa-performance-lab/docs/learning/STATUS.md`:

```markdown
# 현재 학습 상태

- 마지막 갱신일: 2026-07-24
- 현재 학습 단계: 2단계 — `Post` 엔티티와 기존 테이블 매핑

## 완료된 작업

- `jpa-performance-lab` 프로젝트를 Java 26, Spring Boot 4.1.0, Gradle Groovy DSL로 생성했다.
- Spring Web MVC, Thymeleaf, Spring Data JPA, PostgreSQL Driver, Validation, DevTools, Actuator 의존성을 구성했다.
- Supabase GDTCS 프로젝트에 `jpa_study` 스키마를 생성했다.
- `jpa_study.posts` 테이블과 테스트 데이터 3건을 생성하고 DBeaver에서 조회했다.
- 애플리케이션의 Actuator health에서 전체 상태와 `db` 구성요소가 `UP`인 것을 확인했다.
- DB 접속 계정과 비밀번호를 `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD` 환경변수로 분리했다.

## 현재 확인된 실행 결과

- PostgreSQL 연결: 성공
- 대상 스키마: `jpa_study`
- Hibernate DDL 정책: `validate`
- Supavisor 연결: Shared Pooler Session Mode 5432
- HikariCP 최대 풀 크기: 5

## 다음에 할 한 단계

사용자가 `Post` 엔티티를 직접 작성해 `jpa_study.posts` 테이블의 `id`, `title`, `content`, `author`, `created_at`, `updated_at` 컬럼을 매핑한다.

## 다음 단계 완료 조건

- `Post` 클래스가 `jpa_study.posts` 테이블에 매핑된다.
- 기본 키 생성 전략이 기존 `bigint generated by default as identity` 컬럼과 일치한다.
- `created_at`, `updated_at`이 `timestamp with time zone`과 호환되는 Java 타입으로 매핑된다.
- 애플리케이션 또는 컨텍스트 테스트가 Hibernate `validate`를 통과한다.

## 알려진 문제 또는 막힌 점

- 애플리케이션이 사용하는 DB 계정이 `jpa_study` 전용 최소 권한 계정인지 아직 확인하지 않았다.
- JPA 엔티티와 Repository는 아직 작성하지 않았다.
```

- [ ] **Step 3: DECISIONS 문서 작성**

Create `jpa-performance-lab/docs/learning/DECISIONS.md`:

```markdown
# 기술 결정 기록

## 2026-07-24 — ODoc 모노레포 유지

- 결정: `jpa-performance-lab`을 별도 Git 저장소가 아닌 `ODOC` 모노레포의 하위 프로젝트로 유지한다.
- 이유: 여러 실습 프로젝트를 한 저장소에서 관리하고 어느 PC에서든 한 번의 동기화로 이어가기 위해서다.
- 변경 조건: 프로젝트가 독립 배포나 별도 권한 관리가 필요해질 때 분리를 검토한다.

## 2026-07-24 — 프로젝트 기술 기준

- 결정: 프로젝트명은 `jpa-performance-lab`, 기본 패키지는 `com.study.jpalab`, Java는 26, Spring Boot는 4.1.0을 사용한다.
- 이유: JPA 학습과 이후 성능 실험을 한 프로젝트에서 단계적으로 진행하기 위해서다.
- 변경 조건: 사용 중인 라이브러리나 배포 환경이 해당 버전을 지원하지 않을 때 변경을 검토한다.

## 2026-07-24 — Gradle 구성

- 결정: Gradle Groovy DSL과 Gradle Wrapper 9.5.1을 사용한다.
- 이유: PC마다 별도 Gradle을 설치하지 않고 동일한 빌드 환경을 재현하기 위해서다.
- 변경 조건: Java 또는 Spring Boot 업그레이드가 더 높은 Gradle 버전을 요구할 때 변경한다.

## 2026-07-24 — 서버 렌더링 UI

- 결정: 초기 게시판 화면은 Thymeleaf와 Spring Web MVC로 구성한다.
- 이유: JPA 학습에 집중하면서도 브라우저에서 CRUD 결과를 확인하기 위해서다.
- 변경 조건: API와 프런트엔드를 분리해 실험할 단계가 오면 별도 프런트엔드를 검토한다.

## 2026-07-24 — 학습용 데이터베이스

- 결정: Supabase GDTCS 프로젝트의 PostgreSQL과 `jpa_study` 전용 스키마를 사용한다.
- 이유: 무료 프로젝트 수 제한 안에서 기존 업무 스키마와 학습 객체를 논리적으로 분리하기 위해서다.
- 변경 조건: 부하 테스트가 GDTCS 프로젝트에 영향을 줄 가능성이 있으면 격리된 로컬 또는 유료 DB로 옮긴다.

## 2026-07-24 — JPA 스키마 정책

- 결정: `spring.jpa.hibernate.ddl-auto=validate`와 `hibernate.default_schema=jpa_study`를 사용한다.
- 이유: 테이블은 SQL로 직접 관리하고 JPA 매핑 오류만 시작 시점에 검증하기 위해서다.
- 변경 조건: 마이그레이션 도구를 도입하더라도 운영 테이블 자동 변경은 허용하지 않는다.

## 2026-07-24 — Supabase 연결 방식

- 결정: IPv4 환경의 지속 실행 Spring 애플리케이션에서 Shared Pooler Session Mode 5432와 SSL을 사용한다.
- 이유: JPA와 JDBC의 지속 연결을 유지하면서 IPv4 네트워크에서 접속하기 위해서다.
- 변경 조건: 실행 환경이 IPv6 직접 연결을 안정적으로 지원하거나 배포 구조가 서버리스로 바뀌면 연결 모드를 재검토한다.

## 2026-07-24 — 초기 커넥션 풀

- 결정: HikariCP `maximum-pool-size`를 5로 시작한다.
- 이유: 공유 Supabase 무료 환경에서 연결을 과도하게 점유하지 않고 기준 성능을 측정하기 위해서다.
- 변경 조건: Actuator, Supabase 관측 지표, 부하 테스트 결과로 대기 연결이 확인될 때 근거를 남기고 조정한다.

## 2026-07-24 — 비밀정보 전달

- 결정: DB 사용자명과 비밀번호는 `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD` 운영체제 사용자 환경변수로 전달한다.
- 이유: 여러 PC에서 IntelliJ 실행 설정에 의존하지 않으면서 실제 비밀번호를 Git에 저장하지 않기 위해서다.
- 변경 조건: 팀 비밀관리 도구를 도입하면 해당 도구를 단일 전달 수단으로 바꾼다.

## 2026-07-24 — AI 학습 인계

- 결정: `docs/learning`을 학습 상태의 단일 기준으로 사용하고 Codex와 Claude Code는 같은 문서를 읽는다.
- 이유: AI 대화 기록이나 특정 PC의 로컬 상태에 의존하지 않고 학습을 이어가기 위해서다.
- 변경 조건: 다른 AI 도구를 추가해도 공통 문서를 복제하지 않고 진입 파일만 추가한다.

## 2026-07-24 — Git 커밋과 푸시

- 결정: AI가 만드는 커밋 메시지는 한글로 작성하고, 사용자가 명시적으로 요청할 때만 푸시한다.
- 이유: 저장소 이력을 사용자가 읽기 쉽게 유지하고 원격 변경 시점을 사용자가 통제하기 위해서다.
- 변경 조건: 저장소 전체에 별도 커밋 규칙과 자동 배포 절차가 도입되면 그 규칙을 따른다.
```

- [ ] **Step 4: 공통 문서의 구조와 일관성 검증**

Run:

```powershell
Set-Location C:\sw\ODOC
$requiredFiles = @(
  'jpa-performance-lab/docs/learning/ROADMAP.md',
  'jpa-performance-lab/docs/learning/STATUS.md',
  'jpa-performance-lab/docs/learning/DECISIONS.md'
)
$requiredFiles | ForEach-Object {
  if (-not (Test-Path $_)) { throw "필수 문서 없음: $_" }
}
rg -n "2단계|Post|jpa_study.posts|다음에 할 한 단계|완료 조건" jpa-performance-lab/docs/learning
$placeholderPattern = @('T' + 'BD', 'TO' + 'DO', 'FIX' + 'ME', '미' + '정', '추후' + ' 작성') -join '|'
$placeholderMatches = rg -n $placeholderPattern jpa-performance-lab/docs/learning
if ($LASTEXITCODE -eq 0) {
  $placeholderMatches
  throw '불완전 문구가 남아 있습니다.'
}
'learning_docs_placeholder_scan=PASS'
```

Expected:

- 첫 번째 `rg`가 `ROADMAP.md`, `STATUS.md`, `DECISIONS.md`에서 의도한 상태와 결정을 찾는다.
- 두 번째 `rg`는 결과가 없어 종료 코드 1을 반환한다.
- `ROADMAP.md`의 2단계는 `진행 중`이고 `STATUS.md`의 현재 단계도 2단계다.

- [ ] **Step 5: 한글 커밋 메시지로 공통 문서 커밋**

Run:

```powershell
git add -- jpa-performance-lab/docs/learning/ROADMAP.md jpa-performance-lab/docs/learning/STATUS.md jpa-performance-lab/docs/learning/DECISIONS.md
git diff --cached --check
git diff --cached --stat
git commit -m "JPA 학습 로드맵과 현재 상태 기록"
```

Expected: 학습 문서 세 개만 포함한 새 커밋이 생성되고 원격 저장소에는 아직 푸시되지 않는다.

### Task 3: Codex와 Claude Code 진입 파일 생성

**Files:**
- Create: `jpa-performance-lab/AGENTS.md`
- Create: `jpa-performance-lab/CLAUDE.md`

**Interfaces:**
- Consumes: Task 2의 `ROADMAP.md`, `STATUS.md`, `DECISIONS.md`
- Produces: Codex와 Claude Code가 동일한 시작·학습·종료 규칙을 따르는 자동 진입점

- [ ] **Step 1: Codex 프로젝트 지침 작성**

Create `jpa-performance-lab/AGENTS.md`:

```markdown
# JPA Performance Lab 작업 지침

## 세션 시작

응답이나 작업 전에 다음 파일을 순서대로 읽는다.

1. `README.md`
2. `docs/learning/STATUS.md`
3. `docs/learning/ROADMAP.md`
4. `docs/learning/DECISIONS.md`

그다음 `git status --short`와 최근 커밋을 확인한다. 사용자에게는 먼저 다음 세 가지만 간결하게 보고한다.

- 지금까지 완료된 것
- 현재 학습 단계
- 이번 세션에서 할 한 가지

문서와 코드가 다르면 코드, 검증 결과, Git 이력을 우선해 `STATUS.md`를 정정한다. 미커밋 변경이 있으면 출처를 확인하기 전에 덮어쓰거나 정리하지 않는다.

## 학습 방식

- 사용자가 직접 실습한다. 요청하지 않은 학습 코드를 대신 작성하지 않는다.
- 한 번에 한 단계만 설명하고 사용자의 실행 결과를 확인한 뒤 다음 단계로 넘어간다.
- JPA 개념은 현재 작성하는 코드와 SQL 테이블을 연결해서 설명한다.
- 단계 완료 조건을 먼저 명확히 하고, 검증되지 않은 작업은 완료로 표시하지 않는다.
- 의미 있는 단계가 끝나면 `docs/learning/STATUS.md`를 갱신한다.
- 학습 단계 상태가 바뀌면 `docs/learning/ROADMAP.md`를 같은 커밋에서 갱신한다.
- 새로운 기술 선택이 생기면 `docs/learning/DECISIONS.md`에 날짜, 결정, 이유, 변경 조건을 기록한다.

## 검증

- Java/Gradle 변경은 `.\gradlew.bat test`로 검증한다.
- 애플리케이션 연결 확인은 `/actuator/health`의 전체 상태와 `db` 상태가 `UP`인지 확인한다.
- 검증에 실패하면 실행 명령, 핵심 오류, 재현 방법을 `STATUS.md`의 알려진 문제에 기록한다.
- 실패한 작업을 완료로 표시하거나 성공했다고 추측하지 않는다.

## Git

- 기존 사용자 변경을 보존하고 작업과 관련된 경로만 스테이징한다.
- AI가 만드는 커밋 메시지의 제목과 본문은 한글로 작성한다. 기술 식별자와 명령어는 필요한 경우 원문을 유지한다.
- 푸시 전에 `git diff`, `git status`, 푸시할 커밋 목록을 확인한다.
- AI가 만든 미푸시 영문 커밋은 한글로 고칠 수 있지만 기존 사용자 커밋 이력은 임의로 다시 작성하지 않는다.
- 사용자가 명시적으로 요청한 경우에만 원격 저장소에 푸시한다.

## 보안과 부하 테스트

- 실제 비밀번호, API 키, 토큰을 출력하거나 Git 추적 파일과 커밋 메시지에 기록하지 않는다.
- DB 자격 증명은 `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD` 환경변수로 받는다.
- 공유 GDTCS 무료 프로젝트에서는 Smoke·Baseline 수준만 실행한다.
- Stress·Spike·Soak 테스트는 격리된 로컬 또는 별도 DB에서만 진행한다.
```

- [ ] **Step 2: Claude Code 진입 지침 작성**

Create `jpa-performance-lab/CLAUDE.md`:

```markdown
# Claude Code 프로젝트 지침

이 프로젝트의 상세 작업 원칙은 `AGENTS.md`를 기준으로 한다. 응답이나 작업 전에 다음 파일을 읽는다.

1. `AGENTS.md`
2. `README.md`
3. `docs/learning/STATUS.md`
4. `docs/learning/ROADMAP.md`
5. `docs/learning/DECISIONS.md`

학습 상태를 이 파일에 중복 기록하지 않는다. Codex와 동일하게 공통 학습 문서를 단일 기준으로 사용한다.

세션 시작 시 완료된 내용, 현재 단계, 이번에 할 한 가지를 먼저 요약한다. 사용자가 직접 실습하므로 요청하지 않은 코드를 대신 작성하지 않고 한 단계씩 안내한다.

커밋 메시지는 한글로 작성하고, 푸시 전 범위와 커밋 메시지를 확인하며, 사용자가 명시적으로 요청한 경우에만 푸시한다. 실제 비밀번호와 토큰은 출력하거나 Git에 기록하지 않는다.
```

- [ ] **Step 3: 두 AI 진입점이 같은 공통 문서를 참조하는지 검증**

Run:

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
$references = @(
  'README.md',
  'docs/learning/STATUS.md',
  'docs/learning/ROADMAP.md',
  'docs/learning/DECISIONS.md'
)
$references | ForEach-Object {
  if (-not (Test-Path $_)) { throw "참조 대상 없음: $_" }
}
rg -n "STATUS.md|ROADMAP.md|DECISIONS.md|직접 실습|커밋 메시지.*한글|명시적으로 요청" AGENTS.md CLAUDE.md
```

Expected:

- 참조 대상 네 개가 모두 존재한다.
- 두 진입 파일에서 공통 상태 문서와 직접 실습 원칙을 찾는다.
- 한글 커밋 메시지와 사용자 요청 시에만 푸시한다는 규칙을 찾는다.

- [ ] **Step 4: 한글 커밋 메시지로 AI 진입 파일 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/AGENTS.md jpa-performance-lab/CLAUDE.md
git diff --cached --check
git diff --cached --stat
git commit -m "Codex와 Claude Code 학습 인계 지침 추가"
```

Expected: AI 진입 파일 두 개만 포함한 새 커밋이 생성되고 원격 저장소에는 아직 푸시되지 않는다.

### Task 4: 새 PC 실행 및 학습 안내서 작성

**Files:**
- Create: `jpa-performance-lab/README.md`

**Interfaces:**
- Consumes: Task 1의 환경변수 계약, Task 2의 학습 상태 문서, Task 3의 AI 작업 규칙
- Produces: 새 PC에서 저장소 동기화부터 애플리케이션 실행과 다음 학습 단계 확인까지 수행할 수 있는 단일 사용자 안내서

- [ ] **Step 1: README 작성**

Create `jpa-performance-lab/README.md`:

````markdown
# JPA Performance Lab

JPA 기본 CRUD부터 쿼리·인덱스 개선과 부하 테스트까지 단계적으로 학습하는 Spring Boot 게시판 프로젝트다. 사용자가 직접 코드를 작성하고 Codex 또는 Claude Code는 `docs/learning`의 상태를 읽어 한 단계씩 안내한다.

## 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 26 |
| Framework | Spring Boot 4.1.0 |
| Build | Gradle Groovy DSL, Wrapper 9.5.1 |
| Web | Spring Web MVC, Thymeleaf |
| Persistence | Spring Data JPA, Hibernate |
| Database | Supabase PostgreSQL, `jpa_study` 스키마 |
| Observability | Spring Boot Actuator |
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

별도 Gradle 설치는 필요하지 않다.

```powershell
java -version
.\gradlew.bat --version
```

Java 26과 Gradle 9.5.1이 표시되어야 한다.

## 데이터베이스 접속정보

애플리케이션은 Supabase Shared Pooler Session Mode 5432, SSL, `jpa_study` 스키마를 사용한다. 실제 DB 사용자명과 비밀번호는 Git에 저장하지 않고 다음 운영체제 사용자 환경변수에서 읽는다.

- `JPA_STUDY_DB_USERNAME`
- `JPA_STUDY_DB_PASSWORD`

새 PC에서는 PowerShell을 열어 다음 명령을 한 번 실행한다. 자격 증명 창에는 `jpa_study` 전용 DB 계정을 입력한다.

```powershell
$dbCredential = Get-Credential -Message 'jpa_study 전용 데이터베이스 계정 입력'
[Environment]::SetEnvironmentVariable('JPA_STUDY_DB_USERNAME', $dbCredential.UserName, 'User')
[Environment]::SetEnvironmentVariable('JPA_STUDY_DB_PASSWORD', $dbCredential.GetNetworkCredential().Password, 'User')
Remove-Variable dbCredential
```

명령 실행 후 IntelliJ와 터미널을 다시 시작한다. 이 값은 IntelliJ 실행 설정이 아니라 Windows 사용자 환경변수이므로 같은 PC의 새 터미널에서도 사용할 수 있다.

실제 비밀번호를 YAML, Markdown, 커밋 메시지에 기록하지 않는다. 관리자 계정보다 `jpa_study`만 접근 가능한 제한 계정을 사용한다.

## 테스트

```powershell
.\gradlew.bat test
```

`BUILD SUCCESSFUL`이 표시되어야 한다. 컨텍스트 테스트는 DB 연결과 Hibernate `ddl-auto=validate` 설정도 확인한다.

## 애플리케이션 실행

```powershell
.\gradlew.bat bootRun
```

실행 후 다음 주소에서 상태를 확인한다.

- Health: `http://localhost:8080/actuator/health`

전체 `status`와 `components.db.status`가 모두 `UP`이면 PostgreSQL 연결이 성공한 것이다.

## 현재 학습 상태 확인

- 전체 순서: `docs/learning/ROADMAP.md`
- 현재 단계와 다음 한 단계: `docs/learning/STATUS.md`
- 기술 선택의 이유: `docs/learning/DECISIONS.md`

Codex 또는 Claude Code를 시작하면 위 문서를 읽고 다음 세 가지를 먼저 요약해야 한다.

1. 지금까지 완료된 것
2. 현재 학습 단계
3. 이번 세션에서 할 한 가지

현재 다음 단계는 사용자가 `Post` 엔티티를 직접 작성해 기존 `jpa_study.posts` 테이블에 매핑하는 것이다.

## 학습 종료와 Git 인계

의미 있는 단계가 끝나면 테스트 결과를 확인하고 `STATUS.md`를 갱신한다. 단계 상태가 바뀌면 `ROADMAP.md`, 새로운 기술 결정이 있으면 `DECISIONS.md`도 함께 갱신한다.

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
````

- [ ] **Step 2: README 명령과 실제 프로젝트 설정 일치 여부 검증**

Run:

```powershell
Set-Location C:\sw\ODOC
rg -n "Java 26|Spring Boot 4.1.0|Gradle 9.5.1|Session Mode 5432|JPA_STUDY_DB_USERNAME|JPA_STUDY_DB_PASSWORD|ddl-auto=validate|actuator/health|Post.*엔티티|커밋.*한글" jpa-performance-lab/README.md
rg -n "JavaLanguageVersion.of\\(26\\)|org.springframework.boot.*4.1.0" jpa-performance-lab/build.gradle
rg -n "gradle-9.5.1-bin.zip" jpa-performance-lab/gradle/wrapper/gradle-wrapper.properties
rg -n "5432|currentSchema=jpa_study|default_schema: jpa_study|ddl-auto: validate|maximum-pool-size: 5" jpa-performance-lab/src/main/resources/application.yaml
```

Expected: README에 적은 버전, 스키마, 풀 크기, 환경변수, 상태 확인 주소가 실제 설정 파일과 일치한다.

- [ ] **Step 3: 한글 커밋 메시지로 README 커밋**

Run:

```powershell
git add -- jpa-performance-lab/README.md
git diff --cached --check
git diff --cached --stat
git commit -m "새 PC 실행과 JPA 학습 절차 문서화"
```

Expected: `README.md`만 포함한 새 커밋이 생성되고 원격 저장소에는 아직 푸시되지 않는다.

### Task 5: 인계 체계 전체 검증

**Files:**
- Verify: `jpa-performance-lab/AGENTS.md`
- Verify: `jpa-performance-lab/CLAUDE.md`
- Verify: `jpa-performance-lab/README.md`
- Verify: `jpa-performance-lab/docs/learning/ROADMAP.md`
- Verify: `jpa-performance-lab/docs/learning/STATUS.md`
- Verify: `jpa-performance-lab/docs/learning/DECISIONS.md`
- Verify: `jpa-performance-lab/src/main/resources/application.yaml`

**Interfaces:**
- Consumes: Task 1~4의 프로젝트 기준선, 공통 상태 문서, AI 진입점, 실행 안내서
- Produces: 다른 PC로 푸시하기 전에 검토 가능한 완료 증거

- [ ] **Step 1: 필수 파일과 내부 참조 검증**

Run:

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
$requiredFiles = @(
  'AGENTS.md',
  'CLAUDE.md',
  'README.md',
  'docs/learning/ROADMAP.md',
  'docs/learning/STATUS.md',
  'docs/learning/DECISIONS.md',
  'src/main/resources/application.yaml'
)
$requiredFiles | ForEach-Object {
  if (-not (Test-Path $_)) { throw "필수 파일 없음: $_" }
}
'required_files=PASS'
```

Expected:

```text
required_files=PASS
```

- [ ] **Step 2: 비밀값·불완전 문구·문서 상태 불일치 검증**

Run:

```powershell
$yamlText = Get-Content -Raw -Encoding UTF8 'src/main/resources/application.yaml'
if ($yamlText -notmatch 'password:\s*"\$\{JPA_STUDY_DB_PASSWORD\}"') { throw 'DB 비밀번호가 환경변수 참조가 아닙니다.' }
if ($yamlText -notmatch 'username:\s*"\$\{JPA_STUDY_DB_USERNAME\}"') { throw 'DB 사용자명이 환경변수 참조가 아닙니다.' }
$placeholderPattern = @('T' + 'BD', 'TO' + 'DO', 'FIX' + 'ME', '미' + '정', '추후' + ' 작성') -join '|'
$placeholderMatches = rg -n $placeholderPattern AGENTS.md CLAUDE.md README.md docs/learning
if ($LASTEXITCODE -eq 0) {
  $placeholderMatches
  throw '불완전 문구가 남아 있습니다.'
}
rg -n "2단계|진행 중|Post" docs/learning/ROADMAP.md docs/learning/STATUS.md
```

Expected:

- 첫 번째 `rg`는 결과가 없어 종료 코드 1을 반환한다.
- 두 번째 `rg`는 ROADMAP과 STATUS에서 동일한 2단계 상태를 찾는다.
- YAML에는 실제 비밀번호가 없고 환경변수 참조만 있다.

- [ ] **Step 3: 빌드와 DB 연결을 포함한 전체 테스트**

Run:

```powershell
.\gradlew.bat clean test
```

Expected:

- `BUILD SUCCESSFUL`
- `JpaPerformanceLabApplicationTests.contextLoads()` 통과
- Hibernate schema validation 통과
- PostgreSQL 연결 오류 없음

- [ ] **Step 4: Git 변경 범위와 한글 커밋 메시지 검증**

Run:

```powershell
Set-Location C:\sw\ODOC
git diff --check
git status --short -- jpa-board-study jpa-performance-lab
git log 657a5fe8..HEAD --format="%h %s"
```

Expected:

- `git diff --check` 오류가 없다.
- `jpa-board-study`, `jpa-performance-lab` 아래에 의도하지 않은 미커밋·미추적 변경이 없다.
- 구현 계획 이후 AI가 만든 커밋의 제목은 모두 한글이다.
- 원격 저장소에는 아직 푸시하지 않는다.

- [ ] **Step 5: 검증 중 수정이 발생한 경우에만 후속 커밋**

검증 과정에서 문서나 설정을 수정하지 않았다면 이 단계는 실행하지 않는다. 수정이 있었다면 관련 파일만 스테이징한다.

Run:

```powershell
git add -- jpa-performance-lab/AGENTS.md jpa-performance-lab/CLAUDE.md jpa-performance-lab/README.md jpa-performance-lab/docs/learning/ROADMAP.md jpa-performance-lab/docs/learning/STATUS.md jpa-performance-lab/docs/learning/DECISIONS.md jpa-performance-lab/src/main/resources/application.yaml
git diff --cached --check
git diff --cached --stat
git commit -m "학습 인계 문서 검증 결과 반영"
```

Expected: 검증 수정이 있을 때만 한글 메시지의 후속 커밋이 생성된다. 푸시는 사용자의 별도 요청을 기다린다.
