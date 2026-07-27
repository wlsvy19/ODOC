# 애플리케이션 접속 주소 콘솔 출력 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Gradle `bootRun` 시작 로그의 맨 끝에 실제 Tomcat 포트를 사용한 게시판 접속 주소를 출력한다.

**Architecture:** 기존 `PostConsoleRunner`는 게시글 조회만 담당하고 새 `ApplicationUrlConsoleRunner`는 접속 주소 출력만 담당한다. 두 `CommandLineRunner`에 실행 순서를 부여하고, 새 컴포넌트가 Spring Boot 웹 서버 컨텍스트에서 실제 포트를 읽어 게시글 출력 다음에 주소를 안내한다.

**Tech Stack:** Java 26, Spring Boot 4.1.0, Spring Web MVC, Gradle 9.5.1, JUnit Jupiter 6, Mockito 5, AssertJ

## Global Constraints

- IntelliJ IDEA Community의 Gradle `Tasks → application → bootRun` 실행을 기준으로 한다.
- 출력 주소는 `http://localhost:<실제 포트>/posts` 형식이다.
- 기본 포트 `8080`을 Java 코드에 고정하지 않는다.
- 접속 주소는 게시글 전체 조회 결과 다음에 한 번만 출력한다.
- 브라우저를 자동으로 실행하지 않는다.
- Actuator health 주소는 이번 출력에 포함하지 않는다.
- 기존 게시글 조회와 P6Spy SQL 출력 동작을 변경하지 않는다.
- Java 26, Spring Boot 4.1.0과 Gradle Wrapper 9.5.1을 유지하고 새 의존성을 추가하지 않는다.
- AI가 만드는 커밋 메시지는 한글로 작성한다.
- 사용자의 명시적인 요청 전에는 원격 저장소에 푸시하지 않는다.

---

## File Structure

### 생성 파일

- `src/main/java/com/study/jpalab/config/ApplicationUrlConsoleRunner.java`: 실제 웹 서버 포트를 읽고 게시판 접속 주소를 출력한다.
- `src/test/java/com/study/jpalab/config/ApplicationUrlConsoleRunnerTest.java`: 동적 포트 출력, 두 시작 작업의 실행 순서와 비웹 테스트 컨텍스트 동작을 검증한다.

### 변경 파일

- `src/main/java/com/study/jpalab/post/PostConsoleRunner.java`: 게시글 조회 시작 작업의 순서를 첫 번째로 지정한다.
- `README.md`: IntelliJ Community Gradle 실행 방법과 시작 로그의 접속 주소를 안내한다.
- `docs/learning/STATUS.md`: UTF-8 콘솔과 접속 주소 출력의 실제 확인 결과를 기록한다.
- `docs/learning/DECISIONS.md`: 여러 PC에서 공통으로 사용하는 Gradle UTF-8 실행 결정을 기록한다.

---

### Task 1: 실제 포트 기반 접속 주소 출력

**Files:**

- Create: `src/test/java/com/study/jpalab/config/ApplicationUrlConsoleRunnerTest.java`
- Create: `src/main/java/com/study/jpalab/config/ApplicationUrlConsoleRunner.java`
- Modify: `src/main/java/com/study/jpalab/post/PostConsoleRunner.java`

**Interfaces:**

- Consumes: `ApplicationContext`, `WebServerApplicationContext#getWebServer()`, `WebServer#getPort()`, 기존 `PostConsoleRunner`
- Produces: `ApplicationUrlConsoleRunner#run(String... args)`와 게시글 조회 뒤에 출력되는 `http://localhost:<port>/posts`

- [ ] **Step 1: 동적 포트와 실행 순서 테스트 작성**

`src/test/java/com/study/jpalab/config/ApplicationUrlConsoleRunnerTest.java`를 다음 내용으로 만든다.

```java
package com.study.jpalab.config;

import com.study.jpalab.post.PostConsoleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUrlConsoleRunnerTest {

    @Mock
    private WebServerApplicationContext applicationContext;

    @Mock
    private WebServer webServer;

    @Mock
    private ApplicationContext nonWebApplicationContext;

    @Test
    void printsBoardUrlUsingActualWebServerPort() {
        when(applicationContext.getWebServer()).thenReturn(webServer);
        when(webServer.getPort()).thenReturn(54321);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream testOut = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(testOut);
            new ApplicationUrlConsoleRunner(applicationContext).run();
        } finally {
            System.setOut(originalOut);
        }

        String lineSeparator = System.lineSeparator();
        assertThat(output.toString(StandardCharsets.UTF_8)).isEqualTo(
                lineSeparator
                        + "=== 애플리케이션 접속 주소 ===" + lineSeparator
                        + "게시판: http://localhost:54321/posts" + lineSeparator
        );
    }

    @Test
    void runsAfterPostConsoleRunner() {
        Order postOrder = PostConsoleRunner.class.getAnnotation(Order.class);
        Order urlOrder = ApplicationUrlConsoleRunner.class.getAnnotation(Order.class);

        assertThat(postOrder).isNotNull();
        assertThat(urlOrder).isNotNull();
        assertThat(postOrder.value()).isLessThan(urlOrder.value());
    }

    @Test
    void skipsOutputWhenWebServerIsNotRunning() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream testOut = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(testOut);
            new ApplicationUrlConsoleRunner(nonWebApplicationContext).run();
        } finally {
            System.setOut(originalOut);
        }

        assertThat(output.toString(StandardCharsets.UTF_8)).isEmpty();
    }
}
```

- [ ] **Step 2: 새 테스트가 실패하는지 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.config.ApplicationUrlConsoleRunnerTest"
```

Expected: `ApplicationUrlConsoleRunner`를 찾을 수 없어 `compileTestJava`가 실패한다.

- [ ] **Step 3: 게시글 조회 작업의 실행 순서 지정**

`PostConsoleRunner.java`에 `Order` import를 추가한다.

```java
import org.springframework.core.annotation.Order;
```

`@Component` 아래에 첫 번째 시작 작업임을 지정한다.

```java
@Component
@Order(1)
public class PostConsoleRunner implements CommandLineRunner {
```

- [ ] **Step 4: 접속 주소 출력 컴포넌트 구현**

`src/main/java/com/study/jpalab/config/ApplicationUrlConsoleRunner.java`를 다음 내용으로 만든다.

```java
package com.study.jpalab.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class ApplicationUrlConsoleRunner implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    public ApplicationUrlConsoleRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) {
        if (!(applicationContext instanceof WebServerApplicationContext webServerApplicationContext)) {
            return;
        }

        int port = webServerApplicationContext.getWebServer().getPort();

        System.out.println();
        System.out.println("=== 애플리케이션 접속 주소 ===");
        System.out.printf("게시판: http://localhost:%d/posts%n", port);
    }
}
```

- [ ] **Step 5: 대상 테스트가 성공하는지 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.config.ApplicationUrlConsoleRunnerTest"
```

Expected: `BUILD SUCCESSFUL`, 테스트 3개 성공.

- [ ] **Step 6: 전체 회귀 테스트 실행**

Run:

```powershell
.\gradlew.bat test
```

Expected: `BUILD SUCCESSFUL`, 기존 매핑·Repository·엔티티·애플리케이션 컨텍스트 테스트와 새 테스트가 모두 성공.

- [ ] **Step 7: 임시 포트로 실제 시작 로그 확인**

사용자 소유의 8080 프로세스와 충돌하지 않도록 다음 명령으로 실행한다.

```powershell
.\gradlew.bat bootRun --args="--server.port=0"
```

Expected:

1. Tomcat이 임의의 포트로 시작한다.
2. 게시글 전체 조회 결과가 출력된다.
3. 로그의 맨 끝에 다음 내용이 출력된다.
4. `<실제 포트>`가 Tomcat 시작 로그의 포트와 같다.

```text
=== 애플리케이션 접속 주소 ===
게시판: http://localhost:<실제 포트>/posts
```

확인 후 `Ctrl+C`로 `bootRun`을 종료한다.

- [ ] **Step 8: 구현 변경 커밋**

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/src/main/java/com/study/jpalab/config/ApplicationUrlConsoleRunner.java jpa-performance-lab/src/main/java/com/study/jpalab/post/PostConsoleRunner.java jpa-performance-lab/src/test/java/com/study/jpalab/config/ApplicationUrlConsoleRunnerTest.java
git diff --cached --check
git commit -m "실행 후 게시판 접속 주소 출력"
```

---

### Task 2: 공통 실행 방법과 학습 상태 문서화

**Files:**

- Modify: `README.md`
- Modify: `docs/learning/STATUS.md`
- Modify: `docs/learning/DECISIONS.md`

**Interfaces:**

- Consumes: 검증된 Gradle UTF-8 설정과 Task 1의 실제 포트 기반 주소 출력
- Produces: 다른 PC에서 IntelliJ Community로 동일하게 실행할 수 있는 안내와 최신 학습 인계 상태

- [ ] **Step 1: README에 IntelliJ Community 실행 방법 추가**

`README.md`의 `애플리케이션 실행` 절에서 PowerShell 명령 바로 앞에 다음 문단을 추가한다.

```markdown
IntelliJ IDEA Community에서는 Gradle 도구 창의 `Tasks → application → bootRun`을 더블클릭한다. 이 방식은 프로젝트에 저장된 UTF-8 콘솔 설정을 사용하므로 PC별 VM 옵션을 추가하지 않는다.
```

주소 목록 바로 앞에는 다음 문단을 추가한다.

```markdown
시작이 완료되면 콘솔 맨 끝에 실제 실행 포트가 적용된 JSP 게시판 주소가 출력된다. 포트를 변경해 실행해도 출력 주소가 자동으로 바뀐다.
```

- [ ] **Step 2: Gradle UTF-8 기술 결정 기록**

`docs/learning/DECISIONS.md` 끝에 다음 내용을 추가한다.

```markdown
## 2026-07-27 — Gradle bootRun UTF-8 콘솔

- 결정: Gradle 데몬, Java 컴파일, Gradle 테스트와 `bootRun` JVM의 기본 문자 집합, 표준 출력과 표준 오류 인코딩을 UTF-8로 고정한다.
- 이유: 어느 PC에서든 Git으로 프로젝트를 받은 뒤 IntelliJ 개인 VM 옵션 없이 한글 로그를 동일하게 출력하기 위해서다.
- 변경 조건: 실행 방식을 IntelliJ Application 직접 실행이나 배포 컨테이너로 바꾸면 해당 실행 환경에 같은 인코딩 정책을 별도로 적용한다.
```

- [ ] **Step 3: 현재 학습 상태 갱신**

`docs/learning/STATUS.md`를 다음과 같이 갱신한다.

- `완료된 작업`에 Gradle 데몬·컴파일·테스트·`bootRun` UTF-8 설정을 Git으로 공유하도록 구성했다는 사실을 추가한다.
- `완료된 작업`에 시작 로그 맨 끝에서 실제 포트가 적용된 게시판 주소를 출력하도록 구성했다는 사실을 추가한다.
- `현재 확인된 실행 결과`에 IntelliJ Community의 Gradle `bootRun` 한글 출력 성공과 접속 주소 출력 성공을 추가한다.
- `알려진 문제 또는 막힌 점`에서 IntelliJ UTF-8 VM 옵션이 Git으로 추적되지 않는다는 해결된 항목을 제거한다.
- 현재 단계와 `다음에 할 한 단계`는 게시글 수정과 JPA 변경 감지로 유지한다.

- [ ] **Step 4: 문서와 전체 변경 검증**

Run:

```powershell
Set-Location C:\sw\ODOC
git diff --check
git diff --stat
git status --short
```

Expected:

- README, STATUS, DECISIONS만 새 문서 변경으로 표시된다.
- 현재 학습 단계와 다음 한 단계가 게시글 수정으로 유지된다.
- DB 접속값이 문서나 새 diff에 포함되지 않는다.

- [ ] **Step 5: 문서 커밋**

```powershell
git add -- jpa-performance-lab/README.md jpa-performance-lab/docs/learning/STATUS.md jpa-performance-lab/docs/learning/DECISIONS.md
git diff --cached --check
git commit -m "Gradle 실행 방법과 접속 주소 문서화"
```

원격 푸시는 사용자가 현재 진행 내용을 Git에 올리라고 명시적으로 요청한 경우에만 실행한다.
