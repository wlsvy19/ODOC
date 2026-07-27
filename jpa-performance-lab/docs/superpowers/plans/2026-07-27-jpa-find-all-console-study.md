# JPA 전체 조회와 콘솔 출력 구현 계획

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.
>
> **학습 실행 예외:** Java 소스와 테스트 코드는 사용자가 직접 타이핑한다. AI 작업자는 사용자가 명시적으로 구현을 요청하지 않는 한 Java 파일을 생성하거나 수정하지 않고, 한 번에 한 단계씩 설명과 검증만 담당한다.

**Goal:** 사용자가 JPA 엔티티와 Spring Data JPA Repository를 직접 작성하고, 애플리케이션 시작 시 `findAll()`로 `jpa_study.posts` 전체 데이터를 조회해 IntelliJ 콘솔에서 확인한다.

**Architecture:** `com.study.jpalab.post` 기능 패키지에 `Post`, `PostRepository`, `PostConsoleRunner`를 둔다. `PostConsoleRunner`는 Spring이 생성한 Repository를 생성자 주입받아 시작 시 한 번 전체 조회하며, Hibernate SQL 로그와 게시글 필드를 콘솔에 출력한다.

**Tech Stack:** Java 26, Spring Boot 4.1.0, Spring Data JPA, Hibernate, Gradle 9.5.1, PostgreSQL, Supabase Shared Pooler Session Mode 5432

## Global Constraints

- 모든 Java 실습 코드와 테스트 코드는 사용자가 직접 타이핑한다.
- 사용자가 명시적으로 구현을 요청하지 않는 한 AI는 Java 소스와 테스트 파일을 생성하거나 수정하지 않는다.
- AI는 한 번에 한 파일 또는 한 단계만 안내하고 사용자의 실행 결과를 확인한 후 다음 단계로 넘어간다.
- 대상은 기존 `jpa_study.posts` 테이블이며 테이블 구조를 변경하지 않는다.
- Hibernate는 `ddl-auto=validate`를 유지하고 JPA 매핑 오류를 숨기지 않는다.
- 전체 조회는 `JpaRepository.findAll()`만 사용하고 Native Query와 JPQL은 만들지 않는다.
- DB username과 password를 응답, 문서, 로그, 커밋 메시지에 출력하지 않는다.
- Java 변경은 `.\gradlew.bat test`로 검증한다.
- AI가 만드는 커밋 메시지는 한글로 작성한다.
- 사용자가 명시적으로 요청하기 전에는 원격 저장소에 푸시하지 않는다.

## 파일 구성

| 파일 | 책임 |
|---|---|
| `AGENTS.md` | 사용자 직접 타이핑과 단계별 안내 규칙 |
| `docs/learning/DECISIONS.md` | 직접 타이핑 학습 방식을 프로젝트 결정으로 기록 |
| `src/test/java/com/study/jpalab/post/PostMappingTest.java` | `Post`가 JPA 엔티티로 등록되고 Hibernate 검증을 통과하는지 확인 |
| `src/main/java/com/study/jpalab/post/Post.java` | `jpa_study.posts` 테이블의 6개 컬럼 매핑 |
| `src/test/java/com/study/jpalab/post/PostRepositoryTest.java` | `findAll()`이 기존 게시글을 조회하는지 확인 |
| `src/main/java/com/study/jpalab/post/PostRepository.java` | `JpaRepository<Post, Long>` 전체 조회 인터페이스 |
| `src/main/java/com/study/jpalab/post/PostConsoleRunner.java` | 애플리케이션 시작 시 게시글 전체 조회와 콘솔 출력 |
| `src/main/resources/application.yaml` | Hibernate SELECT 문을 확인하기 위한 SQL 로그 설정만 추가 |
| `docs/learning/STATUS.md` | 완료된 엔티티·Repository·콘솔 조회와 다음 단계 기록 |
| `docs/learning/ROADMAP.md` | 2·3단계 완료 및 4단계 진행 상태 반영 |

---

### Task 1: 사용자 직접 타이핑 학습 규칙 확정

**Files:**
- Modify: `AGENTS.md`
- Modify: `docs/learning/DECISIONS.md`

**Interfaces:**
- Consumes: `docs/superpowers/specs/2026-07-27-jpa-find-all-console-study-design.md`의 학습 진행 원칙
- Produces: 이후 모든 Java 작업에 적용되는 사용자 직접 타이핑 규칙

- [ ] **Step 1: `AGENTS.md`의 학습 방식 규칙 보강**

`AGENTS.md`의 `## 학습 방식` 첫 부분을 다음 규칙으로 정리한다.

```markdown
- 모든 Java 실습 코드와 테스트 코드는 사용자가 직접 타이핑한다.
- 사용자가 명시적으로 구현을 요청하지 않는 한 AI는 Java 소스와 테스트 파일을 생성하거나 수정하지 않는다.
- AI는 한 번에 한 파일 또는 한 단계만 설명한다.
- 사용자의 실행 결과를 확인한 뒤 다음 단계로 넘어간다.
- JPA 개념은 현재 작성하는 코드와 SQL 테이블을 연결해서 설명한다.
```

기존의 상태·결정 문서 갱신 규칙과 검증 규칙은 그대로 유지한다.

- [ ] **Step 2: `DECISIONS.md`에 공식 학습 방식 기록**

`docs/learning/DECISIONS.md` 끝에 다음 내용을 추가한다.

```markdown
## 2026-07-27 — 사용자 직접 타이핑 학습

- 결정: 모든 Java 실습 코드와 테스트 코드는 사용자가 직접 타이핑하고, AI는 한 번에 한 파일 또는 한 단계만 안내한다.
- 이유: 완성된 코드를 대신 받는 방식이 아니라 JPA 코드의 구조와 실행 결과를 사용자가 직접 경험하기 위해서다.
- 변경 조건: 사용자가 특정 작업의 구현을 AI에 명시적으로 요청한 경우에만 해당 범위의 파일 수정을 허용한다.
```

- [ ] **Step 3: 문서 규칙 검증**

Run:

```powershell
rg -n "직접 타이핑|명시적으로 구현|한 파일 또는 한 단계|실행 결과" AGENTS.md docs/learning/DECISIONS.md
git diff --check -- AGENTS.md docs/learning/DECISIONS.md
```

Expected:

- 두 문서에서 사용자 직접 타이핑 규칙을 찾는다.
- whitespace 오류가 없다.
- Java 파일 변경이 없다.

- [ ] **Step 4: 문서 규칙 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/AGENTS.md jpa-performance-lab/docs/learning/DECISIONS.md
git diff --cached --check
git commit -m "사용자 직접 타이핑 학습 규칙 추가"
```

Expected: 두 Markdown 파일만 한글 메시지로 커밋되고 푸시하지 않는다.

---

### Task 2: `Post` 엔티티와 기존 테이블 매핑

**Files:**
- Create: `src/test/java/com/study/jpalab/post/PostMappingTest.java`
- Create: `src/main/java/com/study/jpalab/post/Post.java`

**Interfaces:**
- Consumes: PostgreSQL `jpa_study.posts(id, title, content, author, created_at, updated_at)`
- Produces: JPA 엔티티 `Post`와 getter `getId()`, `getTitle()`, `getContent()`, `getAuthor()`, `getCreatedAt()`, `getUpdatedAt()`

- [ ] **Step 1: 사용자가 실패하는 엔티티 매핑 테스트 작성**

사용자가 `src/test/java/com/study/jpalab/post/PostMappingTest.java`를 직접 생성하고 다음 코드를 타이핑한다.

```java
package com.study.jpalab.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostMappingTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void postEntityIsMapped() {
        var postEntity = entityManager.getMetamodel().entity(Post.class);

        assertThat(postEntity.getName()).isEqualTo("Post");
    }
}
```

- [ ] **Step 2: 테스트가 컴파일 단계에서 실패하는지 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostMappingTest"
```

Expected: `Post` 클래스가 아직 없으므로 `compileTestJava`에서 `cannot find symbol` 또는 같은 의미의 컴파일 오류가 발생한다.

- [ ] **Step 3: 사용자가 `Post` 엔티티 작성**

사용자가 `src/main/java/com/study/jpalab/post/Post.java`를 직접 생성하고 다음 코드를 타이핑한다.

```java
package com.study.jpalab.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "posts", schema = "jpa_study")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Post() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
```

- [ ] **Step 4: 엔티티 매핑과 Hibernate `validate` 통과 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostMappingTest"
```

Expected:

- `PostMappingTest`가 통과한다.
- Supabase 연결 오류가 없다.
- Hibernate schema validation 오류가 없다.

- [ ] **Step 5: 엔티티와 매핑 테스트 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/src/main/java/com/study/jpalab/post/Post.java jpa-performance-lab/src/test/java/com/study/jpalab/post/PostMappingTest.java
git diff --cached --check
git commit -m "Post 엔티티 매핑 추가"
```

Expected: 엔티티와 매핑 테스트만 한글 메시지로 커밋되고 푸시하지 않는다.

---

### Task 3: Spring Data JPA Repository 전체 조회

**Files:**
- Create: `src/test/java/com/study/jpalab/post/PostRepositoryTest.java`
- Create: `src/main/java/com/study/jpalab/post/PostRepository.java`

**Interfaces:**
- Consumes: Task 2의 `Post`
- Produces: `PostRepository extends JpaRepository<Post, Long>`과 상속 메서드 `List<Post> findAll()`

- [ ] **Step 1: 사용자가 실패하는 Repository 조회 테스트 작성**

사용자가 `src/test/java/com/study/jpalab/post/PostRepositoryTest.java`를 직접 생성하고 다음 코드를 타이핑한다.

```java
package com.study.jpalab.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findAllPosts() {
        List<Post> posts = postRepository.findAll();

        assertThat(posts).isNotEmpty();
        assertThat(posts).allSatisfy(post -> {
            assertThat(post.getId()).isNotNull();
            assertThat(post.getTitle()).isNotBlank();
            assertThat(post.getContent()).isNotBlank();
            assertThat(post.getAuthor()).isNotBlank();
            assertThat(post.getCreatedAt()).isNotNull();
            assertThat(post.getUpdatedAt()).isNotNull();
        });
    }
}
```

- [ ] **Step 2: Repository가 없어서 실패하는지 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostRepositoryTest"
```

Expected: `PostRepository`가 아직 없으므로 `compileTestJava`에서 `cannot find symbol` 또는 같은 의미의 컴파일 오류가 발생한다.

- [ ] **Step 3: 사용자가 Repository 작성**

사용자가 `src/main/java/com/study/jpalab/post/PostRepository.java`를 직접 생성하고 다음 코드를 타이핑한다.

```java
package com.study.jpalab.post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
```

- [ ] **Step 4: `findAll()` 조회 테스트 통과 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostRepositoryTest"
```

Expected:

- `findAllPosts()`가 통과한다.
- 기존 게시글이 하나 이상 조회된다.
- 게시글의 6개 필드가 정상적으로 변환된다.

- [ ] **Step 5: Repository와 조회 테스트 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/src/main/java/com/study/jpalab/post/PostRepository.java jpa-performance-lab/src/test/java/com/study/jpalab/post/PostRepositoryTest.java
git diff --cached --check
git commit -m "게시글 전체 조회 Repository 추가"
```

Expected: Repository와 조회 테스트만 한글 메시지로 커밋되고 푸시하지 않는다.

---

### Task 4: 애플리케이션 시작 시 전체 조회와 콘솔 출력

**Files:**
- Create: `src/main/java/com/study/jpalab/post/PostConsoleRunner.java`
- Modify: `src/main/resources/application.yaml`

**Interfaces:**
- Consumes: Task 3의 `PostRepository.findAll()`과 Task 2의 `Post` getter
- Produces: 애플리케이션 시작 시 Hibernate SELECT 로그와 게시글 필드 출력

- [ ] **Step 1: 사용자가 Hibernate SQL 로그 설정 추가**

사용자가 `src/main/resources/application.yaml`의 기존 datasource 설정과 같은 최상위 깊이에 다음 블록을 추가한다. 기존 username과 password는 수정하거나 출력하지 않는다.

```yaml
logging:
  level:
    org.hibernate.SQL: debug
```

- [ ] **Step 2: 사용자가 `PostConsoleRunner` 작성**

사용자가 `src/main/java/com/study/jpalab/post/PostConsoleRunner.java`를 직접 생성하고 다음 코드를 타이핑한다.

```java
package com.study.jpalab.post;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostConsoleRunner implements CommandLineRunner {

    private final PostRepository postRepository;

    public PostConsoleRunner(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) {
        List<Post> posts = postRepository.findAll();

        System.out.println("=== 게시글 전체 조회 결과 ===");

        if (posts.isEmpty()) {
            System.out.println("조회된 게시글이 없습니다.");
            return;
        }

        posts.forEach(post -> System.out.printf(
                "id=%d, title=%s, content=%s, author=%s, createdAt=%s, updatedAt=%s%n",
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        ));
    }
}
```

- [ ] **Step 3: 전체 자동 테스트 실행**

Run:

```powershell
.\gradlew.bat test
```

Expected:

- 전체 테스트가 통과한다.
- 컨텍스트 시작 중 `PostConsoleRunner`가 실행되어도 오류가 없다.
- Hibernate schema validation 오류가 없다.

- [ ] **Step 4: 애플리케이션 실행과 콘솔 결과 확인**

IntelliJ에서 `JpaPerformanceLabApplication`을 실행하거나 다음 명령을 사용한다.

```powershell
.\gradlew.bat bootRun
```

Expected:

- Hibernate 로그에 `jpa_study.posts`를 조회하는 SELECT가 보인다.
- `=== 게시글 전체 조회 결과 ===`가 보인다.
- 기존 게시글 3건의 6개 필드가 각각 출력된다.
- DB 접속정보는 콘솔에 출력되지 않는다.

확인이 끝나면 IntelliJ의 Stop 버튼을 누르거나 터미널에서 `Ctrl+C`로 종료한다.

- [ ] **Step 5: Runner와 SQL 로그 설정 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/src/main/java/com/study/jpalab/post/PostConsoleRunner.java jpa-performance-lab/src/main/resources/application.yaml
git diff --cached --check
git commit -m "게시글 전체 조회 콘솔 출력 추가"
```

Expected: Runner와 SQL 로그 설정만 한글 메시지로 커밋되고 푸시하지 않는다.

---

### Task 5: 학습 상태와 다음 단계 갱신

**Files:**
- Modify: `docs/learning/STATUS.md`
- Modify: `docs/learning/ROADMAP.md`

**Interfaces:**
- Consumes: Task 2~4의 실제 테스트와 콘솔 실행 결과
- Produces: 다른 PC와 다음 AI 세션이 이어받을 현재 학습 상태

- [ ] **Step 1: `STATUS.md` 갱신**

검증된 결과만 사용해 다음 내용을 반영한다.

```markdown
- 마지막 갱신일: 2026-07-27
- 현재 학습 단계: 4단계 — 게시글 등록과 조회
```

`완료된 작업`에는 다음 항목을 추가한다.

```markdown
- `Post` 엔티티를 `jpa_study.posts` 테이블에 매핑하고 Hibernate `validate`를 통과했다.
- `PostRepository`의 `findAll()`로 기존 게시글을 전체 조회했다.
- `PostConsoleRunner`로 애플리케이션 시작 시 SELECT와 게시글 필드를 콘솔에서 확인했다.
```

`다음에 할 한 단계`는 다음과 같이 변경한다.

```markdown
사용자가 `PostRepository.save()`를 이용한 게시글 등록을 직접 작성하고, 저장된 행과 반환된 식별자를 확인한다.
```

`알려진 문제 또는 막힌 점`에서 “JPA 엔티티와 Repository는 아직 작성하지 않았다” 항목을 제거하고 다음 내용을 추가한다.

```markdown
- `PostConsoleRunner`는 학습 확인용 임시 코드이므로 웹 조회 단계에서 제거하거나 테스트 코드로 옮길지 결정해야 한다.
```

- [ ] **Step 2: `ROADMAP.md` 단계 상태 갱신**

로드맵 표의 상태를 다음과 같이 변경한다.

```markdown
| 2 | 완료 | `Post` 엔티티와 `jpa_study.posts` 테이블 매핑 |
| 3 | 완료 | Spring Data JPA Repository 작성 |
| 4 | 진행 중 | 게시글 등록과 조회 |
```

다른 단계는 변경하지 않는다.

- [ ] **Step 3: 최종 검증**

Run:

```powershell
.\gradlew.bat test
rg -n "2026-07-27|PostRepository|findAll|PostConsoleRunner|save\\(\\)|임시 코드" docs/learning/STATUS.md
rg -n "\\| 2 \\| 완료|\\| 3 \\| 완료|\\| 4 \\| 진행 중" docs/learning/ROADMAP.md
git diff --check
```

Expected:

- 전체 Gradle 테스트가 통과한다.
- 상태 문서가 실제 검증 결과와 일치한다.
- 다음 단계가 `save()` 게시글 등록으로 기록된다.
- whitespace 오류가 없다.

- [ ] **Step 4: 학습 상태 문서 커밋**

Run:

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/docs/learning/STATUS.md jpa-performance-lab/docs/learning/ROADMAP.md
git diff --cached --check
git commit -m "게시글 전체 조회 학습 상태 기록"
```

Expected: 학습 상태 문서 두 개만 한글 메시지로 커밋되고 푸시하지 않는다.

## 완료 기준

- 사용자가 `Post`, `PostRepository`, `PostConsoleRunner`와 두 테스트 파일을 직접 타이핑했다.
- 전체 Gradle 테스트가 성공한다.
- Hibernate가 `jpa_study.posts`의 전체 조회 SELECT를 실행한다.
- 콘솔에서 기존 게시글 3건의 필드 값을 확인한다.
- Native Query와 JPQL을 사용하지 않는다.
- DB 접속정보가 응답, 문서, 로그, 커밋 메시지에 노출되지 않는다.
- `STATUS.md`와 `ROADMAP.md`가 검증된 현재 상태를 반영한다.
- 모든 AI 커밋 메시지는 한글이며 원격 저장소에는 푸시하지 않는다.
