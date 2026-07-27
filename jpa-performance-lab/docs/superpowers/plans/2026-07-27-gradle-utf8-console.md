# Gradle bootRun UTF-8 Console Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 어느 PC에서든 Git으로 프로젝트를 받은 뒤 Gradle `bootRun`으로 실행하면 Java 소스와 애플리케이션 표준 출력·표준 오류가 UTF-8을 사용하게 한다.

**Architecture:** Git으로 추적되는 `gradle.properties`가 Gradle 데몬 JVM의 인코딩을, `build.gradle`이 컴파일·테스트 JVM과 `bootRun` 애플리케이션 JVM의 인코딩을 명시한다. IntelliJ의 개인 `.idea` 설정에는 의존하지 않으며 IntelliJ IDEA Community의 Gradle 도구 창과 Gradle Wrapper 명령이 같은 설정을 사용한다.

**Tech Stack:** Java 26, Spring Boot 4.1.0 Gradle Plugin, Gradle 9.5.1 Groovy DSL, IntelliJ IDEA Community

## Global Constraints

- 사용자는 Gradle `bootRun`으로 애플리케이션을 실행한다.
- Java 소스 인코딩과 애플리케이션 표준 출력·표준 오류 인코딩은 UTF-8로 고정한다.
- Gradle 데몬 JVM의 기본 문자 집합과 표준 출력·표준 오류 인코딩도 UTF-8로 고정한다. `bootRun` 자식 JVM 설정만으로는 Gradle 데몬이 출력하는 한글을 보장할 수 없다.
- Gradle 테스트 JVM에도 같은 인코딩 속성을 적용한다.
- `.idea`와 PC별 IntelliJ Run Configuration은 Git에 추가하지 않는다.
- 기존 Java 26, Spring Boot 4.1.0과 Gradle Wrapper 9.5.1을 유지한다.
- 사용자가 `build.gradle` 변경을 직접 타이핑하고 AI는 저장 결과를 확인한다.
- AI가 만드는 커밋 메시지는 한글로 작성한다.
- 사용자의 명시적인 요청 전에는 원격 저장소에 푸시하지 않는다.

---

## File Structure

### 변경 파일

- `build.gradle`: Java 컴파일, Gradle 테스트와 `bootRun` JVM의 UTF-8 설정
- `gradle.properties`: 기본 Gradle 메모리 한도와 Gradle 데몬 JVM의 UTF-8 설정
- `README.md`: IntelliJ Community Gradle `bootRun` 실행 방식
- `docs/learning/STATUS.md`: UTF-8 검증 결과와 다음 JPA 학습 단계
- `docs/learning/DECISIONS.md`: PC 간 UTF-8 콘솔 결정

### 생성 파일

- `gradle.properties`: 프로젝트 수준 Gradle 데몬 JVM 설정

---

### Task 1: Gradle UTF-8 실행 설정

**Files:**

- Modify: `build.gradle`
- Create: `gradle.properties`

**Interfaces:**

- Consumes: 기존 `JavaCompile`, `test`, Spring Boot `bootRun` Gradle 작업
- Produces: UTF-8 Java 컴파일과 UTF-8 표준 출력·표준 오류를 사용하는 Gradle 데몬, 테스트 및 애플리케이션 JVM

- [ ] **Step 0: Gradle 데몬 UTF-8 설정 추가**

프로젝트 루트에 `gradle.properties`를 만들고 Gradle 기본 메모리 한도와 함께 데몬 JVM의 세 인코딩 속성을 지정한다.

```properties
org.gradle.jvmargs=-Xmx512m -XX:MaxMetaspaceSize=384m -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8
```

- [ ] **Step 1: 기존 테스트 작업 설정 위치 확인**

`build.gradle` 아래쪽에 다음 블록이 있는지 확인한다.

```groovy
tasks.named('test') {
	useJUnitPlatform()
}
```

- [ ] **Step 2: Java 컴파일과 bootRun UTF-8 설정 추가**

기존 `tasks.named('test')` 블록 바로 위에 다음 코드를 추가한다.

```groovy
tasks.withType(JavaCompile).configureEach {
	options.encoding = 'UTF-8'
}

tasks.named('bootRun') {
	systemProperty 'file.encoding', 'UTF-8'
	systemProperty 'stdout.encoding', 'UTF-8'
	systemProperty 'stderr.encoding', 'UTF-8'
}
```

- [ ] **Step 3: 기존 test 블록에 UTF-8 속성 추가**

기존 `tasks.named('test')` 블록 전체를 다음 상태로 맞춘다.

```groovy
tasks.named('test') {
	useJUnitPlatform()
	systemProperty 'file.encoding', 'UTF-8'
	systemProperty 'stdout.encoding', 'UTF-8'
	systemProperty 'stderr.encoding', 'UTF-8'
}
```

- [ ] **Step 4: 전체 Gradle 테스트 실행**

Run:

```powershell
.\gradlew.bat test
```

Expected:

- `BUILD SUCCESSFUL`
- `PostMappingTest`, `PostRepositoryTest`, `PostTest` 성공
- 한글 관련 컴파일 오류가 없음

- [ ] **Step 5: Gradle 데몬 및 bootRun JVM 인수 확인**

기존 데몬을 중지한 후, 사용자 소유 8080 프로세스와 충돌하지 않도록 임시 포트로 다음 명령을 실행한다.

```powershell
.\gradlew.bat --stop
.\gradlew.bat bootRun --info --args="--server.port=0"
```

Expected: 새 Gradle 데몬 및 `bootRun` Java 프로세스 실행 명령에 다음 세 JVM 속성이 포함된다.

```text
-Dfile.encoding=UTF-8
-Dstdout.encoding=UTF-8
-Dstderr.encoding=UTF-8
```

- [ ] **Step 6: 애플리케이션 한글 출력 확인**

애플리케이션 시작 후 다음을 확인한다.

1. `PostConsoleRunner`가 출력하는 기존 게시글의 한글이 깨지지 않는다.
2. `http://localhost:8080/posts`에 접속한다.
3. 게시글 등록 또는 조회로 출력되는 P6Spy SQL의 한글 값이 깨지지 않는다.
4. 확인 후 `Ctrl+C`로 `bootRun`을 종료한다.

- [ ] **Step 7: 설정 변경 커밋**

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/build.gradle jpa-performance-lab/gradle.properties
git diff --cached --check
git commit -m "Gradle bootRun UTF-8 출력 설정"
```

---

### Task 2: 실행 방법과 학습 상태 문서화

**Files:**

- Modify: `README.md`
- Modify: `docs/learning/STATUS.md`
- Modify: `docs/learning/DECISIONS.md`

**Interfaces:**

- Consumes: Task 1에서 검증한 Gradle UTF-8 설정과 실행 결과
- Produces: 다른 PC와 AI가 재현할 수 있는 실행 방법과 다음 학습 단계

- [ ] **Step 1: README에 IntelliJ Community 실행 방법 추가**

`README.md`의 애플리케이션 실행 절에 다음 내용을 추가한다.

```markdown
IntelliJ IDEA Community에서는 Gradle 도구 창의 `Tasks → application → bootRun`을 실행한다. 이 방식은 `build.gradle`에 저장된 UTF-8 표준 출력 설정을 사용하므로 PC별 VM 옵션을 추가하지 않는다. `JpaPerformanceLabApplication` 옆의 초록색 버튼으로 직접 실행하는 방식은 공통 UTF-8 설정의 보장 범위가 아니다.
```

- [ ] **Step 2: 기술 결정 기록 추가**

`docs/learning/DECISIONS.md` 끝에 다음 내용을 추가한다.

```markdown
## 2026-07-27 — Gradle bootRun UTF-8 콘솔

- 결정: Java 컴파일, Gradle 테스트와 `bootRun` JVM의 `file.encoding`, `stdout.encoding`, `stderr.encoding`을 UTF-8로 고정한다.
- 이유: 어느 PC에서든 Git으로 프로젝트를 받은 뒤 IntelliJ 개인 VM 옵션 없이 한글 로그를 동일하게 출력하기 위해서다.
- 변경 조건: 실행 방식을 IntelliJ Application 직접 실행이나 배포 컨테이너로 바꾸면 해당 실행 환경에 같은 인코딩 정책을 별도로 적용한다.
```

- [ ] **Step 3: 현재 상태에 검증 결과 추가**

`docs/learning/STATUS.md`의 완료된 작업과 현재 확인된 실행 결과에 다음 사실을 기록한다.

- Gradle `bootRun` JVM의 기본 문자 집합, 표준 출력과 표준 오류를 UTF-8로 고정했다.
- IntelliJ Community Gradle `bootRun`에서 게시글과 P6Spy SQL의 한글 출력을 확인했다.
- 다음 JPA 학습 단계는 게시글 수정과 변경 감지다.

- [ ] **Step 4: 문서 변경 확인**

Run:

```powershell
Set-Location C:\sw\ODOC
git diff --check
git diff --stat
git status --short
```

Expected:

- README, STATUS, DECISIONS만 문서 변경으로 표시
- DB 접속값이 diff나 문서에 새로 출력되지 않음

- [ ] **Step 5: 문서 커밋**

```powershell
git add -- jpa-performance-lab/README.md jpa-performance-lab/docs/learning/STATUS.md jpa-performance-lab/docs/learning/DECISIONS.md
git diff --cached --check
git commit -m "Gradle UTF-8 실행 방법 문서화"
```

원격 푸시는 사용자가 현재 진행 내용을 Git에 올리라고 명시적으로 요청한 경우에만 실행한다.
