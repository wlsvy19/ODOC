# PC 간 학습 인계 저장 명령 구현 계획

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** `인계 저장`과 `어디까지 진행했어?`를 프로젝트의 공식 중단·재개 명령으로 문서화하고, 현재 JPA 조회 학습 상태를 `origin/main`에 안전하게 저장한다.

**Architecture:** `AGENTS.md`는 AI의 실행 계약, `README.md`는 사용자의 PC 간 절차, `DECISIONS.md`는 명령 도입 이유를 기록한다. `STATUS.md`와 `ROADMAP.md`는 현재 검증 결과와 다음 한 단계를 단일 기준으로 유지하고, 관련 코드·설정·문서를 한글 커밋으로 만든 뒤 일반 푸시한다.

**Tech Stack:** Git main branch, Markdown, Gradle 9.5.1, Java 26, Spring Boot 4.1.0, Spring Data JPA, Supabase PostgreSQL

## Global Constraints

- 정확한 문구 `인계 저장`은 `jpa-performance-lab` 관련 검증·문서 갱신·한글 커밋·`git push origin main`의 명시적 승인이다.
- 정확한 문구 `어디까지 진행했어?`는 Git 상태와 공통 학습 문서를 읽고 완료·현재·다음 한 단계를 보고하는 재개 요청이다.
- 다른 ODoc 프로젝트 변경을 스테이징하거나 커밋하지 않는다.
- 사용자가 작성한 Java 파일은 수정하지 않는다.
- 실패나 알려진 문제를 성공으로 기록하지 않는다.
- DB username과 password를 응답, 학습 문서나 커밋 메시지에 출력하지 않는다.
- 강제 푸시, 자동 충돌 해결과 원격 이력 재작성을 하지 않는다.
- 원격 `main`이 로컬보다 앞서 있으면 푸시를 중단한다.
- AI가 만드는 커밋 제목과 본문은 한글로 작성한다.

---

### Task 1: 중단·재개 명령을 공통 문서에 반영

**Files:**
- Modify: `AGENTS.md`
- Modify: `README.md`
- Modify: `docs/learning/DECISIONS.md`

**Interfaces:**
- Consumes: `docs/superpowers/specs/2026-07-27-learning-handoff-save-command-design.md`
- Produces: Codex·Claude Code가 동일하게 해석하는 `인계 저장`, `어디까지 진행했어?` 계약

- [ ] **Step 1: `AGENTS.md`에 인계 명령 추가**

다음 섹션을 추가한다.

```markdown
## PC 간 학습 인계

- 사용자가 정확히 `인계 저장`이라고 말하면 현재 학습의 검증, 상태 문서 갱신, 한글 커밋과 `git push origin main`까지 명시적으로 승인한 것으로 간주한다.
- 인계 전 `git status --short`, diff, 테스트, 원격 차이를 확인한다.
- 완료·현재·다음 한 단계와 실패 또는 알려진 문제를 `STATUS.md`에 기록한다.
- 단계 상태가 바뀌면 `ROADMAP.md`, 새로운 결정이 생기면 `DECISIONS.md`를 함께 갱신한다.
- 현재 학습 관련 파일만 스테이징하고 다른 ODoc 프로젝트 변경은 보존한다.
- 원격 `main`이 앞서 있거나 충돌 위험, 자격 증명 노출 위험이 있으면 자동 푸시하지 않고 보고한다.
- 강제 푸시와 원격 이력 재작성은 하지 않는다.
- 사용자가 `어디까지 진행했어?`라고 물으면 Git 상태와 공통 학습 문서를 확인하고 완료된 것, 현재 단계, 다음 한 단계를 먼저 보고한다.
```

- [ ] **Step 2: `README.md`에 사용자 절차 추가**

`## 학습 종료와 Git 인계` 아래에 다음 내용을 추가한다.

````markdown
다른 PC에서 이어갈 예정이면 Codex에 다음과 같이 요청한다.

```text
인계 저장
```

이 문구는 현재 학습 상태 확인, 문서 갱신, 한글 커밋과 `origin/main` 푸시까지 승인한다. 푸시가 성공해야 다른 PC에서 이어받을 수 있다.

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
````

- [ ] **Step 3: `DECISIONS.md`에 명령 도입 기록**

다음 내용을 추가한다.

```markdown
## 2026-07-27 — PC 간 학습 인계 명령

- 결정: `인계 저장`을 상태 문서 갱신, 한글 커밋과 `origin/main` 푸시의 명시적 승인 문구로 사용하고, `어디까지 진행했어?`를 재개 요청 문구로 사용한다.
- 이유: 여러 PC에서 대화 기록에 의존하지 않고 Git과 공통 학습 문서만으로 같은 지점부터 이어가기 위해서다.
- 변경 조건: 브랜치 전략이나 자동 동기화 도구가 도입되면 명령의 커밋·푸시 범위를 새 절차에 맞게 변경한다.
```

- [ ] **Step 4: 문서 계약 검증**

Run:

```powershell
rg -n "인계 저장|어디까지 진행했어|git push origin main|git pull --ff-only origin main|강제 푸시|다른 ODoc" AGENTS.md README.md docs/learning/DECISIONS.md
git diff --check -- AGENTS.md README.md docs/learning/DECISIONS.md
```

Expected: 두 명령과 저장·재개·안전 규칙을 세 문서에서 확인하고 whitespace 오류가 없다.

- [ ] **Step 5: 명령 문서 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/AGENTS.md jpa-performance-lab/README.md jpa-performance-lab/docs/learning/DECISIONS.md
git diff --cached --check
git commit -m "PC 간 학습 인계 명령 추가"
```

Expected: 세 문서만 포함한 한글 커밋이 생성된다.

---

### Task 2: 현재 JPA 조회 학습 체크포인트 저장

**Files:**
- Verify: `src/main/java/com/study/jpalab/post/PostConsoleRunner.java`
- Verify: `src/main/resources/application.yaml`
- Modify: `docs/learning/STATUS.md`
- Modify: `docs/learning/ROADMAP.md`

**Interfaces:**
- Consumes: 사용자가 확인한 Hibernate SELECT와 게시글 3건 콘솔 출력
- Produces: 다른 PC가 이어받을 현재 단계, 알려진 문제와 다음 한 단계

- [ ] **Step 1: 현재 변경과 원격 차이 확인**

Run:

```powershell
Set-Location C:\sw\ODOC
git status --short
git diff -- jpa-performance-lab
git fetch origin main
git rev-list --left-right --count origin/main...HEAD
```

Expected:

- 현재 브랜치는 `main`이다.
- `PostConsoleRunner.java`와 `application.yaml`의 학습 변경이 보존돼 있다.
- 원격 `main`이 로컬보다 앞서 있지 않다.

- [ ] **Step 2: 전체 테스트 강제 실행**

Run:

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
$env:JAVA_HOME = 'C:\Users\user\.jdks\openjdk-26.0.2'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat test --rerun-tasks
```

Expected:

- `BUILD SUCCESSFUL`
- 모든 테스트 통과
- Supabase 연결과 Hibernate `validate` 오류 없음

- [ ] **Step 3: `STATUS.md`를 실제 상태로 갱신**

상단을 다음과 같이 변경한다.

```markdown
- 마지막 갱신일: 2026-07-27
- 현재 학습 단계: 4단계 — 게시글 등록과 조회
```

완료된 작업에 다음 내용을 추가한다.

```markdown
- `Post` 엔티티를 `jpa_study.posts` 테이블에 매핑하고 Hibernate `validate`를 통과했다.
- `PostRepository.findAll()`로 기존 게시글을 전체 조회했다.
- `PostConsoleRunner`에서 Hibernate SELECT와 기존 게시글 3건을 IntelliJ 콘솔에 출력했다.
- IntelliJ VM 옵션으로 현재 PC의 콘솔 한글 출력을 확인했다.
```

현재 확인된 실행 결과에 다음 내용을 추가한다.

```markdown
- `PostMappingTest`: 성공
- `PostRepositoryTest`: 성공
- `PostConsoleRunner` 전체 조회: 성공, 게시글 3건
```

다음 한 단계를 다음과 같이 변경한다.

```markdown
`application.yaml`의 `show-sql: true`와 `org.hibernate.SQL: debug` 중복을 정리해 SELECT가 한 번만 출력되게 하고 다시 실행한다.
```

알려진 문제에 다음 내용을 기록한다.

```markdown
- `show-sql: true`와 `org.hibernate.SQL: debug`가 동시에 활성화돼 SELECT가 두 번 출력된다.
- IntelliJ UTF-8 VM 옵션이 Git 추적 파일로 확인되지 않아 다른 PC에서는 실행 설정을 다시 확인해야 한다.
- `PostConsoleRunner`는 학습 확인용 임시 코드이므로 웹 조회 단계에서 제거하거나 테스트 코드로 옮길지 결정해야 한다.
```

기존의 “JPA 엔티티와 Repository는 아직 작성하지 않았다” 항목은 제거한다.

- [ ] **Step 4: `ROADMAP.md` 단계 상태 갱신**

다음 세 행만 변경한다.

```markdown
| 2 | 완료 | `Post` 엔티티와 `jpa_study.posts` 테이블 매핑 |
| 3 | 완료 | Spring Data JPA Repository 작성 |
| 4 | 진행 중 | 게시글 등록과 조회 |
```

- [ ] **Step 5: 체크포인트 범위와 문서 검증**

Run:

```powershell
rg -n "2026-07-27|PostRepository.findAll|PostConsoleRunner|게시글 3건|show-sql|UTF-8 VM 옵션|다음에 할 한 단계" docs/learning/STATUS.md
rg -n "\| 2 \| 완료|\| 3 \| 완료|\| 4 \| 진행 중" docs/learning/ROADMAP.md
git diff --check
```

Expected: 현재 성공 결과와 남은 문제를 모두 찾고 whitespace 오류가 없다.

- [ ] **Step 6: 현재 학습 체크포인트 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/src/main/java/com/study/jpalab/post/PostConsoleRunner.java jpa-performance-lab/src/main/resources/application.yaml jpa-performance-lab/docs/learning/STATUS.md jpa-performance-lab/docs/learning/ROADMAP.md
git diff --cached --check
git commit -m "게시글 전체 조회 학습 상태 저장"
```

Expected: 현재 학습 코드·설정·상태 문서만 포함한 한글 커밋이 생성된다.

- [ ] **Step 7: 원격 `main` 푸시와 확인**

Run:

```powershell
Set-Location C:\sw\ODOC
git fetch origin main
git rev-list --left-right --count origin/main...HEAD
git push origin main
git fetch origin main
git rev-parse HEAD
git rev-parse origin/main
git status --short
```

Expected:

- 푸시 직전 원격 선행 커밋 수가 0이다.
- 일반 푸시가 성공한다.
- 로컬 `HEAD`와 `origin/main`이 일치한다.
- `jpa-performance-lab` 관련 미커밋 변경이 없다.

## 다른 PC 재개 명령

```powershell
Set-Location C:\sw\ODOC
git status --short
git pull --ff-only origin main
Set-Location .\jpa-performance-lab
```

Codex 작업 폴더를 `C:\sw\ODOC\jpa-performance-lab`으로 열고 `어디까지 진행했어?`라고 묻는다.
