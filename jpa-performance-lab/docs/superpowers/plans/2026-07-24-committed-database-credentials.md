# 개인 저장소 DB 접속정보 하드코딩 구현 계획

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** `application.yaml`에 Supabase DB 접속정보를 직접 저장해 새 PC에서 별도 환경변수나 IntelliJ 설정 없이 프로젝트를 실행한다.

**Architecture:** 현재 Windows 사용자 환경변수에 보관된 검증된 접속정보를 값 출력 없이 `application.yaml`의 리터럴 값으로 한 번 이전한다. 실행 문서와 AI 지침은 이 개인 저장소 예외를 명시하고, 애플리케이션은 `application.yaml`을 유일한 DB 접속정보 원본으로 사용한다.

**Tech Stack:** Java 26, Spring Boot 4.1.0, Gradle Groovy DSL, Gradle Wrapper 9.5.1, Spring Data JPA, PostgreSQL, Supabase Shared Pooler Session Mode 5432, YAML, Markdown, PowerShell

## Global Constraints

- Git 루트는 `C:\sw\ODOC`이고 작업 브랜치는 사용자가 승인한 `main`이다.
- `jpa-performance-lab`은 `ODOC` 모노레포의 하위 프로젝트로 유지한다.
- Java 26, Spring Boot 4.1.0, Gradle Groovy DSL, Gradle Wrapper 9.5.1을 유지한다.
- Supabase GDTCS 프로젝트의 `jpa_study` 스키마를 사용한다.
- JDBC URL의 Shared Pooler Session Mode 5432, `sslmode=require`, `currentSchema=jpa_study`를 유지한다.
- Hibernate는 `ddl-auto=validate`, `hibernate.default_schema=jpa_study`를 유지한다.
- HikariCP `maximum-pool-size`는 5를 유지한다.
- `spring.datasource.username`과 `spring.datasource.password`는 `application.yaml`에 실제 리터럴 값으로 저장하고 Git에 커밋한다.
- `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD`는 이전 작업의 입력으로만 사용하며 애플리케이션 실행 요구사항으로 남기지 않는다.
- 실제 username과 password 값을 응답, 작업 보고서, 학습 문서, 커밋 메시지에 출력하지 않는다.
- 자격 증명이 포함된 `application.yaml` diff 본문을 사용자 대화에 붙여 넣지 않는다.
- 저장소는 사용자만 접근하는 개인 저장소라는 전제를 따른다. 공개·공유 전에 비밀번호를 교체하고 환경변수 방식 전환을 검토한다.
- 사용자가 요청하지 않은 `Post` 엔티티, Repository, CRUD 코드는 구현하지 않는다.
- 기존 사용자 변경을 보존하고 각 작업에 적힌 경로만 스테이징한다.
- AI가 만드는 커밋 메시지의 제목과 본문은 한글로 작성한다.
- 사용자가 별도로 요청하기 전에는 원격 저장소에 푸시하지 않는다.
- 기준 설계는 `docs/superpowers/specs/2026-07-24-committed-database-credentials-design.md`이다.

---

## 파일 구조와 책임

| 경로 | 작업 | 책임 |
|---|---|---|
| `jpa-performance-lab/src/main/resources/application.yaml` | 수정 | DB username과 password의 리터럴 값을 포함하는 단일 실행 설정 |
| `jpa-performance-lab/docs/learning/STATUS.md` | 수정 | 현재 하드코딩 상태와 다음 JPA 학습 단계 기록 |
| `jpa-performance-lab/docs/learning/DECISIONS.md` | 수정 | 개인 저장소 하드코딩 결정과 변경 조건 기록 |
| `jpa-performance-lab/docs/superpowers/specs/2026-07-24-multi-pc-ai-learning-handoff-design.md` | 수정 | 기존 보안 정책이 새 설계로 대체됐음을 표시 |
| `jpa-performance-lab/docs/superpowers/plans/2026-07-24-multi-pc-ai-learning-handoff.md` | 수정 | 기존 환경변수 실행 계획의 중단·대체 상태 표시 |
| `jpa-performance-lab/README.md` | 생성 | 환경변수 없이 새 PC에서 실행하는 절차 안내 |
| `jpa-performance-lab/AGENTS.md` | 생성 | Codex의 승인된 하드코딩 정책과 학습 인계 규칙 |
| `jpa-performance-lab/CLAUDE.md` | 생성 | Claude Code의 승인된 하드코딩 정책과 공통 문서 진입 규칙 |

### Task 1: application.yaml DB 접속정보 원상복구

**Files:**
- Modify: `jpa-performance-lab/src/main/resources/application.yaml`

**Interfaces:**
- Consumes: Windows User scope의 `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD`
- Produces: 실제 username과 password 리터럴을 가진 `spring.datasource` 설정

- [ ] **Step 1: 이전에 보관한 접속정보와 현재 환경변수 참조 상태 확인**

Run:

```powershell
Set-Location C:\sw\ODOC
$dbUsernamePresent = -not [string]::IsNullOrWhiteSpace(
  [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_USERNAME', 'User')
)
$dbPasswordPresent = -not [string]::IsNullOrWhiteSpace(
  [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_PASSWORD', 'User')
)
$yamlText = Get-Content -Raw -Encoding UTF8 'jpa-performance-lab/src/main/resources/application.yaml'
$usernameReferencePresent = $yamlText.Contains('${JPA_STUDY_DB_USERNAME}')
$passwordReferencePresent = $yamlText.Contains('${JPA_STUDY_DB_PASSWORD}')
"credential_source_present=$($dbUsernamePresent -and $dbPasswordPresent)"
"yaml_environment_references_present=$($usernameReferencePresent -and $passwordReferencePresent)"
if (-not ($dbUsernamePresent -and $dbPasswordPresent)) { throw '이전할 DB 접속정보가 없습니다.' }
if (-not ($usernameReferencePresent -and $passwordReferencePresent)) { throw '현재 YAML 환경변수 참조를 찾지 못했습니다.' }
```

Expected:

```text
credential_source_present=True
yaml_environment_references_present=True
```

실제 username과 password 값은 출력하지 않는다.

- [ ] **Step 2: 환경변수 없는 현재 설정이 실패하는지 확인**

Run:

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
Remove-Item Env:JPA_STUDY_DB_USERNAME -ErrorAction SilentlyContinue
Remove-Item Env:JPA_STUDY_DB_PASSWORD -ErrorAction SilentlyContinue
.\gradlew.bat test --rerun-tasks
```

Expected: 현재 YAML이 환경변수를 요구하므로 테스트가 DB 인증 또는 datasource 설정 오류로 실패한다. 오류 출력에 실제 비밀번호가 나타나면 즉시 출력을 중단하고 보고서에 복사하지 않는다.

- [ ] **Step 3: 접속정보를 출력 없이 YAML 리터럴로 기계적으로 이전**

이 단계는 비밀값을 패치 본문에 노출하지 않기 위한 기계적 치환이다. 다른 모든 사람이 읽는 파일 수정에는 `apply_patch`를 사용한다.

Run:

```powershell
Set-Location C:\sw\ODOC
$yamlPath = (Resolve-Path 'jpa-performance-lab/src/main/resources/application.yaml').Path
$dbUsername = [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_USERNAME', 'User')
$dbPassword = [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_PASSWORD', 'User')
if ([string]::IsNullOrWhiteSpace($dbUsername) -or [string]::IsNullOrWhiteSpace($dbPassword)) {
  throw '이전할 DB 접속정보가 없습니다.'
}

function ConvertTo-YamlDoubleQuotedScalar([string]$value) {
  $escaped = $value.Replace('\', '\\').Replace('"', '\"').Replace("`r", '\r').Replace("`n", '\n')
  return '"' + $escaped + '"'
}

$usernameScalar = ConvertTo-YamlDoubleQuotedScalar $dbUsername
$passwordScalar = ConvertTo-YamlDoubleQuotedScalar $dbPassword
$yamlText = Get-Content -Raw -Encoding UTF8 $yamlPath
$usernameToken = '    username: "${JPA_STUDY_DB_USERNAME}"'
$passwordToken = '    password: "${JPA_STUDY_DB_PASSWORD}"'
if (-not $yamlText.Contains($usernameToken) -or -not $yamlText.Contains($passwordToken)) {
  throw '치환할 datasource 환경변수 참조가 없습니다.'
}

$yamlText = $yamlText.Replace($usernameToken, '    username: ' + $usernameScalar)
$yamlText = $yamlText.Replace($passwordToken, '    password: ' + $passwordScalar)
[System.IO.File]::WriteAllText($yamlPath, $yamlText, [System.Text.UTF8Encoding]::new($false))

Remove-Variable yamlText, usernameScalar, passwordScalar, dbUsername, dbPassword
'datasource_credentials_migrated=PASS'
```

Expected:

```text
datasource_credentials_migrated=PASS
```

- [ ] **Step 4: YAML 리터럴과 기존 연결 설정을 값 출력 없이 정적 검증**

Run:

```powershell
$yamlPath = 'jpa-performance-lab/src/main/resources/application.yaml'
$yamlText = Get-Content -Raw -Encoding UTF8 $yamlPath
$dbUsername = [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_USERNAME', 'User')
$dbPassword = [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_PASSWORD', 'User')

function ConvertTo-YamlDoubleQuotedScalar([string]$value) {
  $escaped = $value.Replace('\', '\\').Replace('"', '\"').Replace("`r", '\r').Replace("`n", '\n')
  return '"' + $escaped + '"'
}

$expectedUsernameLine = '    username: ' + (ConvertTo-YamlDoubleQuotedScalar $dbUsername)
$expectedPasswordLine = '    password: ' + (ConvertTo-YamlDoubleQuotedScalar $dbPassword)
$literalCredentialsMatch = $yamlText.Contains($expectedUsernameLine) -and $yamlText.Contains($expectedPasswordLine)
$environmentReferencesAbsent = -not $yamlText.Contains('${JPA_STUDY_DB_USERNAME}') -and -not $yamlText.Contains('${JPA_STUDY_DB_PASSWORD}')
$connectionSettingsPresent =
  $yamlText.Contains('sslmode=require') -and
  $yamlText.Contains('currentSchema=jpa_study') -and
  $yamlText.Contains('default_schema: jpa_study') -and
  $yamlText.Contains('ddl-auto: validate') -and
  $yamlText.Contains('maximum-pool-size: 5')

"literal_credentials_match=$literalCredentialsMatch"
"environment_references_absent=$environmentReferencesAbsent"
"connection_settings_present=$connectionSettingsPresent"
if (-not ($literalCredentialsMatch -and $environmentReferencesAbsent -and $connectionSettingsPresent)) {
  throw 'application.yaml 정적 검증 실패'
}
Remove-Variable yamlText, dbUsername, dbPassword, expectedUsernameLine, expectedPasswordLine
```

Expected:

```text
literal_credentials_match=True
environment_references_absent=True
connection_settings_present=True
```

- [ ] **Step 5: 환경변수 없이 Java 26 toolchain과 DB 연결 검증**

Run:

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
Remove-Item Env:JPA_STUDY_DB_USERNAME -ErrorAction SilentlyContinue
Remove-Item Env:JPA_STUDY_DB_PASSWORD -ErrorAction SilentlyContinue
$compileOutput = & .\gradlew.bat compileJava --rerun-tasks --info 2>&1
$compileExitCode = $LASTEXITCODE
$compileOutput | Select-String -Pattern 'Compiling with toolchain|BUILD SUCCESSFUL|BUILD FAILED'
if ($compileExitCode -ne 0) { exit $compileExitCode }
.\gradlew.bat test --rerun-tasks
```

Expected:

- `Compiling with toolchain` 출력이 Java 26 JDK 경로를 가리킨다.
- `BUILD SUCCESSFUL`
- `JpaPerformanceLabApplicationTests.contextLoads()`가 환경변수 없이 Supabase PostgreSQL에 연결한다.

- [ ] **Step 6: 비밀값을 출력하지 않고 application.yaml만 스테이징·커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/src/main/resources/application.yaml
git diff --cached --check
$stagedFiles = @(git diff --cached --name-only)
"staged_file_count=$($stagedFiles.Count)"
$stagedFiles
if ($stagedFiles.Count -ne 1 -or $stagedFiles[0] -ne 'jpa-performance-lab/src/main/resources/application.yaml') {
  throw 'application.yaml 외 파일이 스테이징되었습니다.'
}
git commit -m "DB 접속정보 하드코딩 복구"
```

Expected:

- 스테이징 파일은 `application.yaml` 하나다.
- diff 본문을 터미널이나 보고서에 출력하지 않는다.
- 한글 메시지의 새 커밋이 생성되고 푸시하지 않는다.

### Task 2: 학습 상태와 기존 정책 문서 정정

**Files:**
- Modify: `jpa-performance-lab/docs/learning/STATUS.md`
- Modify: `jpa-performance-lab/docs/learning/DECISIONS.md`
- Modify: `jpa-performance-lab/docs/superpowers/specs/2026-07-24-multi-pc-ai-learning-handoff-design.md`
- Modify: `jpa-performance-lab/docs/superpowers/plans/2026-07-24-multi-pc-ai-learning-handoff.md`

**Interfaces:**
- Consumes: Task 1의 하드코딩된 `application.yaml`
- Produces: 새 정책을 단일 현재 결정으로 안내하고 과거 환경변수 계획을 실행하지 않게 하는 문서

- [ ] **Step 1: STATUS의 완료 작업과 알려진 문제를 새 정책으로 수정**

Modify `jpa-performance-lab/docs/learning/STATUS.md`:

```diff
-- DB 접속 계정과 비밀번호를 `JPA_STUDY_DB_USERNAME`, `JPA_STUDY_DB_PASSWORD` 환경변수로 분리했다.
+- 개인 저장소에서 새 PC의 추가 설정을 없애기 위해 DB 접속 계정과 비밀번호를 `application.yaml`에 직접 저장했다.
@@
-- 애플리케이션이 사용하는 DB 계정이 `jpa_study` 전용 최소 권한 계정인지 아직 확인하지 않았다.
+- 개인 저장소에 DB 접속정보가 포함돼 있으므로 저장소를 공개하거나 공유하기 전 비밀번호 교체와 환경변수 전환이 필요하다.
```

- [ ] **Step 2: DECISIONS의 비밀정보 전달 결정을 하드코딩 결정으로 교체**

Modify `jpa-performance-lab/docs/learning/DECISIONS.md`:

```markdown
## 2026-07-24 — 개인 저장소 DB 접속정보

- 결정: DB 사용자명과 비밀번호를 `application.yaml`에 직접 저장하고 Git에 커밋한다.
- 이유: 사용자만 접근하는 개인 저장소에서 여러 PC가 별도 IntelliJ 설정이나 환경변수 등록 없이 즉시 실행되게 하기 위해서다.
- 변경 조건: 저장소를 공개·공유하거나 협업자가 생기면 현재 비밀번호를 교체하고 환경변수 또는 비밀관리 도구로 전환한다.
```

기존 `## 2026-07-24 — 비밀정보 전달` 섹션의 제목과 세 항목을 위 내용으로 교체한다. 다른 결정은 수정하지 않는다.

- [ ] **Step 3: 기존 설계에 새 정책 우선 안내 추가**

Modify `jpa-performance-lab/docs/superpowers/specs/2026-07-24-multi-pc-ai-learning-handoff-design.md`에서 제목 바로 다음에 추가:

```markdown
> **정책 변경:** DB 접속정보 정책은 `2026-07-24-committed-database-credentials-design.md`가 이 문서보다 우선한다. 개인 저장소의 `application.yaml`에 username과 password를 직접 커밋하며, 환경변수 방식으로 임의 변경하지 않는다.
```

- [ ] **Step 4: 기존 구현 계획에 중단·대체 안내 추가**

Modify `jpa-performance-lab/docs/superpowers/plans/2026-07-24-multi-pc-ai-learning-handoff.md`에서 제목 바로 다음에 추가:

```markdown
> **대체됨:** 이 계획의 DB 환경변수 단계는 `2026-07-24-committed-database-credentials.md`로 대체됐다. `application.yaml` 하드코딩을 승인된 현재 정책으로 사용하며, 이 문서의 환경변수 설정·검증 단계는 다시 실행하지 않는다.
```

- [ ] **Step 5: 문서 간 정책 일치와 실제 접속정보 미포함 검증**

Run:

```powershell
Set-Location C:\sw\ODOC
$activeDocs = @(
  'jpa-performance-lab/docs/learning/STATUS.md',
  'jpa-performance-lab/docs/learning/DECISIONS.md',
  'jpa-performance-lab/docs/superpowers/specs/2026-07-24-multi-pc-ai-learning-handoff-design.md',
  'jpa-performance-lab/docs/superpowers/plans/2026-07-24-multi-pc-ai-learning-handoff.md'
)
rg -n "application.yaml|직접 저장|정책 변경|대체됨|공개|공유" -- $activeDocs

$dbUsername = [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_USERNAME', 'User')
$dbPassword = [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_PASSWORD', 'User')
foreach ($path in $activeDocs) {
  $text = Get-Content -Raw -Encoding UTF8 $path
  if ((-not [string]::IsNullOrEmpty($dbUsername) -and $text.Contains($dbUsername)) -or
      (-not [string]::IsNullOrEmpty($dbPassword) -and $text.Contains($dbPassword))) {
    throw "문서에 실제 접속정보가 포함됨: $path"
  }
}
'documentation_secret_scan=PASS'
```

Expected:

- 새 하드코딩 정책과 공개·공유 시 변경 조건을 찾는다.
- `documentation_secret_scan=PASS`

- [ ] **Step 6: 네 문서만 한글 메시지로 커밋**

Run:

```powershell
git add -- jpa-performance-lab/docs/learning/STATUS.md jpa-performance-lab/docs/learning/DECISIONS.md jpa-performance-lab/docs/superpowers/specs/2026-07-24-multi-pc-ai-learning-handoff-design.md jpa-performance-lab/docs/superpowers/plans/2026-07-24-multi-pc-ai-learning-handoff.md
git diff --cached --check
git diff --cached --stat
git commit -m "DB 접속정보 정책 변경 문서 반영"
```

Expected: 지정한 문서 네 개만 포함한 한글 메시지 커밋이 생성되고 푸시하지 않는다.

### Task 3: 환경변수 없는 새 PC 실행 README 작성

**Files:**
- Create: `jpa-performance-lab/README.md`

**Interfaces:**
- Consumes: Task 1의 하드코딩 datasource와 Task 2의 현재 학습 상태
- Produces: Task 4의 AI 진입 파일이 참조할 사람·AI 공용 실행 안내서

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

- [ ] **Step 2: README와 실제 설정의 공개 가능한 항목 일치 검증**

Run:

```powershell
Set-Location C:\sw\ODOC
rg -n "Java 26|Spring Boot 4.1.0|Gradle 9.5.1|Session Mode 5432|SSL|jpa_study|ddl-auto=validate|최대 풀 크기 5|application.yaml|환경변수.*필요하지 않다|actuator/health|Post.*엔티티|커밋.*한글" jpa-performance-lab/README.md
rg -n "JavaLanguageVersion.of\\(26\\)|org.springframework.boot.*4.1.0" jpa-performance-lab/build.gradle
rg -n "gradle-9.5.1-bin.zip" jpa-performance-lab/gradle/wrapper/gradle-wrapper.properties
rg -n "5432|sslmode=require|currentSchema=jpa_study|default_schema: jpa_study|ddl-auto: validate|maximum-pool-size: 5" jpa-performance-lab/src/main/resources/application.yaml
```

Expected: README의 공개 가능한 버전·스키마·연결·풀 설정이 실제 파일과 일치한다. datasource username과 password 줄은 출력하지 않는다.

- [ ] **Step 3: README만 한글 메시지로 커밋**

Run:

```powershell
git add -- jpa-performance-lab/README.md
git diff --cached --check
git diff --cached --stat
git commit -m "새 PC 무설정 실행 절차 문서화"
```

Expected: `README.md` 하나만 포함한 한글 메시지 커밋이 생성되고 푸시하지 않는다.

### Task 4: Codex와 Claude Code 진입 파일 확정

**Files:**
- Create: `jpa-performance-lab/AGENTS.md`
- Create: `jpa-performance-lab/CLAUDE.md`

**Interfaces:**
- Consumes: Task 2의 학습 문서와 Task 3의 `README.md`
- Produces: 승인된 하드코딩 정책을 임의로 되돌리지 않는 Codex·Claude Code 진입 규칙

- [ ] **Step 1: AGENTS.md를 승인된 정책으로 작성**

Create or replace `jpa-performance-lab/AGENTS.md`:

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

## DB 접속정보와 부하 테스트

- `application.yaml`의 DB username과 password 하드코딩은 사용자가 승인한 개인 저장소 예외다.
- 사용자의 별도 요청 없이 환경변수 방식으로 변경하거나 `application.yaml`을 Git 추적에서 제외하지 않는다.
- 실제 username과 password 값을 응답, 작업 보고서, 학습 문서, 커밋 메시지에 출력하지 않는다.
- 저장소를 공개하거나 공유하기 전에는 비밀번호 교체와 환경변수 또는 비밀관리 도구 전환을 먼저 안내한다.
- 공유 GDTCS 무료 프로젝트에서는 Smoke·Baseline 수준만 실행한다.
- Stress·Spike·Soak 테스트는 격리된 로컬 또는 별도 DB에서만 진행한다.
```

- [ ] **Step 2: CLAUDE.md를 같은 공통 정책으로 작성**

Create or replace `jpa-performance-lab/CLAUDE.md`:

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

`application.yaml`의 DB username과 password 하드코딩은 사용자가 승인한 개인 저장소 예외다. 별도 요청 없이 환경변수 방식으로 바꾸지 않고 실제 접속정보를 응답, 보고서, 학습 문서, 커밋 메시지에 출력하지 않는다. 저장소 공개·공유 전에는 비밀번호 교체와 정책 전환을 먼저 안내한다.

커밋 메시지는 한글로 작성하고, 푸시 전 범위와 커밋 메시지를 확인하며, 사용자가 명시적으로 요청한 경우에만 푸시한다.
```

- [ ] **Step 3: 참조 파일과 하드코딩 정책 일치 검증**

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
rg -n "STATUS.md|ROADMAP.md|DECISIONS.md|직접 실습|하드코딩|환경변수 방식으로.*변경|실제.*접속정보|커밋 메시지.*한글|명시적으로 요청" AGENTS.md CLAUDE.md
```

Expected:

- 참조 대상 네 개가 모두 존재한다.
- 두 파일이 공통 학습 문서를 읽는다.
- 승인된 하드코딩 정책과 실제 접속정보 비출력 규칙을 찾는다.
- 한글 커밋과 사용자 요청 시에만 푸시하는 규칙을 찾는다.

- [ ] **Step 4: AGENTS.md와 CLAUDE.md만 한글 메시지로 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/AGENTS.md jpa-performance-lab/CLAUDE.md
git diff --cached --check
git diff --cached --stat
git commit -m "AI 학습 인계와 DB 접속정보 정책 추가"
```

Expected: 두 AI 진입 파일만 포함한 한글 메시지 커밋이 생성되고 푸시하지 않는다.

### Task 5: 환경변수 없는 인계 체계 전체 검증

**Files:**
- Verify: `jpa-performance-lab/src/main/resources/application.yaml`
- Verify: `jpa-performance-lab/README.md`
- Verify: `jpa-performance-lab/AGENTS.md`
- Verify: `jpa-performance-lab/CLAUDE.md`
- Verify: `jpa-performance-lab/docs/learning/ROADMAP.md`
- Verify: `jpa-performance-lab/docs/learning/STATUS.md`
- Verify: `jpa-performance-lab/docs/learning/DECISIONS.md`

**Interfaces:**
- Consumes: Task 1~4의 하드코딩 설정과 인계 문서
- Produces: 새 PC에서 추가 DB 설정 없이 실행 가능한 상태의 완료 증거

- [ ] **Step 1: 필수 파일과 정책 참조 검증**

Run:

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
$requiredFiles = @(
  'src/main/resources/application.yaml',
  'README.md',
  'AGENTS.md',
  'CLAUDE.md',
  'docs/learning/ROADMAP.md',
  'docs/learning/STATUS.md',
  'docs/learning/DECISIONS.md'
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

- [ ] **Step 2: YAML 리터럴과 활성 문서의 환경변수 비의존성 검증**

Run:

```powershell
$yamlText = Get-Content -Raw -Encoding UTF8 'src/main/resources/application.yaml'
$dbUsername = [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_USERNAME', 'User')
$dbPassword = [Environment]::GetEnvironmentVariable('JPA_STUDY_DB_PASSWORD', 'User')

function ConvertTo-YamlDoubleQuotedScalar([string]$value) {
  $escaped = $value.Replace('\', '\\').Replace('"', '\"').Replace("`r", '\r').Replace("`n", '\n')
  return '"' + $escaped + '"'
}

$literalCredentialsMatch =
  $yamlText.Contains('    username: ' + (ConvertTo-YamlDoubleQuotedScalar $dbUsername)) -and
  $yamlText.Contains('    password: ' + (ConvertTo-YamlDoubleQuotedScalar $dbPassword))
$environmentReferencesAbsent =
  -not $yamlText.Contains('${JPA_STUDY_DB_USERNAME}') -and
  -not $yamlText.Contains('${JPA_STUDY_DB_PASSWORD}')
"literal_credentials_match=$literalCredentialsMatch"
"environment_references_absent=$environmentReferencesAbsent"
if (-not ($literalCredentialsMatch -and $environmentReferencesAbsent)) {
  throw 'YAML 접속정보 정책 검증 실패'
}

$activeFiles = @('README.md', 'AGENTS.md', 'CLAUDE.md', 'docs/learning/STATUS.md', 'docs/learning/DECISIONS.md')
$oldEnvironmentReferences = @(rg -n 'JPA_STUDY_DB_USERNAME|JPA_STUDY_DB_PASSWORD' -- $activeFiles)
if ($oldEnvironmentReferences.Count -gt 0) {
  $oldEnvironmentReferences
  throw '활성 문서에 환경변수 요구사항이 남아 있습니다.'
}
'active_document_environment_scan=PASS'
Remove-Variable yamlText, dbUsername, dbPassword
```

Expected:

```text
literal_credentials_match=True
environment_references_absent=True
active_document_environment_scan=PASS
```

- [ ] **Step 3: 환경변수를 제거한 프로세스에서 전체 테스트**

Run:

```powershell
Remove-Item Env:JPA_STUDY_DB_USERNAME -ErrorAction SilentlyContinue
Remove-Item Env:JPA_STUDY_DB_PASSWORD -ErrorAction SilentlyContinue
.\gradlew.bat clean test
```

Expected:

- `BUILD SUCCESSFUL`
- `JpaPerformanceLabApplicationTests.contextLoads()` 통과
- Supabase PostgreSQL 연결 오류 없음
- Hibernate schema validation 오류 없음

- [ ] **Step 4: Git 범위와 한글 커밋 메시지 검증**

Run:

```powershell
Set-Location C:\sw\ODOC
git diff --check
$status = @(git status --short)
"working_tree_entries=$($status.Count)"
if ($status.Count -gt 0) {
  $status
  throw '미커밋 변경이 남아 있습니다.'
}
git log 65c636fb..HEAD --format="%h %s"
```

Expected:

- whitespace 오류가 없다.
- 미커밋·미추적 변경이 없다.
- 하드코딩 변경 이후 AI가 만든 커밋 제목은 모두 한글이다.
- 원격 저장소에는 아직 푸시하지 않는다.
