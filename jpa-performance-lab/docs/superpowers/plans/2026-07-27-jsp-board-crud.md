# JSP Board CRUD Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Supabase의 `jpa_study.posts`를 사용하는 JSP 게시판에서 목록, 상세, 등록, 수정과 삭제를 구현한다.

**Architecture:** Spring MVC Controller가 JSP와 HTTP 요청을 담당하고, `PostService`가 트랜잭션과 CRUD 사용 사례를 담당하며, 기존 `PostRepository`가 JPA 데이터 접근을 담당한다. 입력은 엔티티에 직접 바인딩하지 않고 `PostForm`으로 받아 검증하며, 쓰기 성공 후에는 PRG 방식으로 이동한다.

**Tech Stack:** Java 26, Spring Boot 4.1.0, Gradle 9.5.1, Spring MVC, JSP, Jakarta Tags 3.0, Spring Data JPA, Hibernate 7.4, Supabase PostgreSQL

## Global Constraints

- 사용자가 Java, JSP, CSS와 테스트 코드를 한 파일 또는 한 단계씩 직접 타이핑한다.
- 사용자가 특정 파일 구현을 명시적으로 맡기지 않는 한 AI는 구현 소스를 수정하지 않는다.
- Java 26, Spring Boot 4.1.0과 Gradle Wrapper 9.5.1을 유지한다.
- 패키지는 `com.study.jpalab`과 `com.study.jpalab.post`를 유지한다.
- Supabase `jpa_study` 스키마와 기존 `posts` 테이블을 유지한다.
- Hibernate `ddl-auto=validate`를 유지하고 JPA가 테이블을 생성하거나 변경하게 하지 않는다.
- `application.yaml`의 기존 DB 접속값은 변경하거나 응답과 문서에 출력하지 않는다.
- 기존 게시글 3건은 수정하거나 삭제하지 않는다.
- 로그인, 댓글, 첨부파일, 검색, 페이징과 비동기 API는 추가하지 않는다.
- 모든 AI 커밋 메시지는 한글로 작성한다.
- 사용자의 명시적인 저장·푸시 요청 전에는 `git push`하지 않는다.
- 각 단계는 실패 확인, 최소 구현, 성공 확인 순서로 진행한다.

---

## File Structure

### 변경 파일

- `build.gradle`: War, JSP, JSTL과 Tomcat 런타임 구성
- `src/main/java/com/study/jpalab/JpaPerformanceLabApplication.java`: 실행형 War 진입점
- `src/main/resources/application.yaml`: JSP ViewResolver와 SQL 로그 중복 제거
- `src/main/java/com/study/jpalab/post/Post.java`: 생성, 수정과 시간 생명주기
- `src/main/java/com/study/jpalab/post/PostController.java`: Spring MVC CRUD 엔드포인트
- `src/main/java/com/study/jpalab/post/PostConsoleRunner.java`: 웹 조회 확인 후 제거
- `README.md`: JSP/War 실행 방식 반영
- `docs/learning/STATUS.md`: 완료 결과와 다음 학습 단계
- `docs/learning/ROADMAP.md`: 4~7단계 결과 반영
- `docs/learning/DECISIONS.md`: Thymeleaf에서 JSP/War로 전환한 결정 기록

### 생성 파일

- `src/main/java/com/study/jpalab/post/PostForm.java`: 등록·수정 입력과 검증
- `src/main/java/com/study/jpalab/post/PostService.java`: CRUD 트랜잭션
- `src/main/java/com/study/jpalab/post/PostNotFoundException.java`: HTTP 404 예외
- `src/main/webapp/WEB-INF/jsp/posts/list.jsp`: 게시글 표 목록
- `src/main/webapp/WEB-INF/jsp/posts/detail.jsp`: 게시글 상세
- `src/main/webapp/WEB-INF/jsp/posts/form.jsp`: 등록·수정 공용 폼
- `src/main/resources/static/css/board.css`: 게시판 공통 스타일
- `src/test/java/com/study/jpalab/post/PostTest.java`: 엔티티 행위 단위 테스트
- `src/test/java/com/study/jpalab/post/PostFormTest.java`: 입력 검증 단위 테스트
- `src/test/java/com/study/jpalab/post/PostServiceTest.java`: 실제 PostgreSQL 기반 Service 통합 테스트
- `src/test/java/com/study/jpalab/post/PostControllerTest.java`: MockMvc Controller 테스트

---

### Task 1: 실행형 War와 JSP 런타임 전환

**Files:**

- Modify: `build.gradle`
- Modify: `src/main/java/com/study/jpalab/JpaPerformanceLabApplication.java`
- Modify: `src/main/resources/application.yaml`

**Interfaces:**

- Consumes: 기존 Spring Boot 애플리케이션과 DB 설정
- Produces: `bootWar` 작업, `/WEB-INF/jsp/` ViewResolver, IntelliJ `main()`과 `bootRun` 실행환경

- [ ] **Step 1: 현재 프로젝트에 `bootWar`가 없음을 확인**

Run:

```powershell
.\gradlew.bat bootWar
```

Expected: FAIL with `Task 'bootWar' not found`.

- [ ] **Step 2: `build.gradle`을 War/JSP 구성으로 변경**

`build.gradle` 전체를 다음 상태로 맞춘다.

```groovy
plugins {
	id 'java'
	id 'war'
	id 'org.springframework.boot' version '4.1.0'
	id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.study'
version = '0.0.1-SNAPSHOT'

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(26)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-webmvc'
	implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
	implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api'
	implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'org.postgresql:postgresql'
	providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat-runtime'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-validation-test'
	testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
	testCompileOnly 'org.projectlombok:lombok'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	testAnnotationProcessor 'org.projectlombok:lombok'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

Thymeleaf 구현 의존성과 `spring-boot-starter-thymeleaf-test`는 남기지 않는다.

- [ ] **Step 3: 애플리케이션 진입점을 War 배포 가능 형태로 변경**

`src/main/java/com/study/jpalab/JpaPerformanceLabApplication.java`:

```java
package com.study.jpalab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class JpaPerformanceLabApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JpaPerformanceLabApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(JpaPerformanceLabApplication.class, args);
    }
}
```

- [ ] **Step 4: 기존 DB 값을 건드리지 않고 JSP ViewResolver를 추가**

`application.yaml`의 기존 최상위 `spring:` 아래에 다음 `mvc` 블록을 추가한다. 기존 `datasource`, `jpa`와 접속정보는 그대로 둔다.

```yaml
  mvc:
    view:
      prefix: /WEB-INF/jsp/
      suffix: .jsp
```

SQL이 두 번 출력되는 기존 문제를 끝내기 위해 `spring.jpa.show-sql`만 다음처럼 변경하고 `logging.level.org.hibernate.SQL: debug`는 유지한다.

```yaml
    show-sql: false
```

- [ ] **Step 5: 전체 테스트와 War 빌드 확인**

Run:

```powershell
.\gradlew.bat test
.\gradlew.bat bootWar
```

Expected:

- 두 명령 모두 `BUILD SUCCESSFUL`
- `build/libs/jpa-performance-lab-0.0.1-SNAPSHOT.war` 생성
- DB 접속정보가 콘솔에 출력되지 않음

- [ ] **Step 6: 실행환경 전환 커밋**

```powershell
git add build.gradle src/main/java/com/study/jpalab/JpaPerformanceLabApplication.java src/main/resources/application.yaml
git diff --cached --check
git commit -m "JSP 실행형 War 환경 구성"
```

---

### Task 2: `Post` 생성·수정·시간 생명주기

**Files:**

- Create: `src/test/java/com/study/jpalab/post/PostTest.java`
- Modify: `src/main/java/com/study/jpalab/post/Post.java`

**Interfaces:**

- Consumes: 기존 `Post` 필드와 `jpa_study.posts` 매핑
- Produces: `Post.create(String, String, String)`, `update(String, String, String)`, JPA 시간 콜백

- [ ] **Step 1: 엔티티 행위 테스트 작성**

`src/test/java/com/study/jpalab/post/PostTest.java`:

```java
package com.study.jpalab.post;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void createPost() {
        Post post = Post.create("제목", "내용", "작성자");

        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getAuthor()).isEqualTo("작성자");
    }

    @Test
    void updatePost() {
        Post post = Post.create("이전 제목", "이전 내용", "이전 작성자");

        post.update("수정 제목", "수정 내용", "수정 작성자");

        assertThat(post.getTitle()).isEqualTo("수정 제목");
        assertThat(post.getContent()).isEqualTo("수정 내용");
        assertThat(post.getAuthor()).isEqualTo("수정 작성자");
    }

    @Test
    void manageTimestampsWithJpaCallbacks() {
        Post post = Post.create("제목", "내용", "작성자");

        post.prePersist();
        var createdAt = post.getCreatedAt();

        post.preUpdate();

        assertThat(createdAt).isNotNull();
        assertThat(post.getUpdatedAt()).isAfterOrEqualTo(createdAt);
    }
}
```

- [ ] **Step 2: 테스트 실패 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostTest"
```

Expected: compilation FAIL because `create`, `update`, `prePersist` and `preUpdate` do not exist.

- [ ] **Step 3: `Post`에 최소 행위와 JPA 콜백 구현**

`src/main/java/com/study/jpalab/post/Post.java`:

```java
package com.study.jpalab.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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

    public static Post create(String title, String content, String author) {
        Post post = new Post();
        post.title = title;
        post.content = content;
        post.author = author;
        return post;
    }

    public void update(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
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

- [ ] **Step 4: 엔티티 테스트와 기존 매핑 테스트 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostTest" --tests "com.study.jpalab.post.PostMappingTest"
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 5: 엔티티 행위 커밋**

```powershell
git add src/main/java/com/study/jpalab/post/Post.java src/test/java/com/study/jpalab/post/PostTest.java
git diff --cached --check
git commit -m "게시글 생성 수정 생명주기 추가"
```

---

### Task 3: `PostForm` 입력 검증

**Files:**

- Create: `src/test/java/com/study/jpalab/post/PostFormTest.java`
- Create: `src/main/java/com/study/jpalab/post/PostForm.java`

**Interfaces:**

- Consumes: `Post`의 title, content와 author
- Produces: JavaBean 형태의 `PostForm`, `PostForm.from(Post)`

- [ ] **Step 1: 폼 검증 테스트 작성**

`src/test/java/com/study/jpalab/post/PostFormTest.java`:

```java
package com.study.jpalab.post;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostFormTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void acceptValidForm() {
        PostForm form = form("제목", "내용", "작성자");

        assertThat(validator.validate(form)).isEmpty();
    }

    @Test
    void rejectBlankFields() {
        PostForm form = form(" ", " ", " ");

        assertThat(validator.validate(form))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder("title", "content", "author");
    }

    @Test
    void rejectTooLongTitleAndAuthor() {
        PostForm form = form("제목".repeat(101), "내용", "작".repeat(51));

        assertThat(validator.validate(form))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder("title", "author");
    }

    private PostForm form(String title, String content, String author) {
        PostForm form = new PostForm();
        form.setTitle(title);
        form.setContent(content);
        form.setAuthor(author);
        return form;
    }
}
```

- [ ] **Step 2: 테스트 실패 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostFormTest"
```

Expected: compilation FAIL because `PostForm` does not exist.

- [ ] **Step 3: `PostForm` 구현**

`src/main/java/com/study/jpalab/post/PostForm.java`:

```java
package com.study.jpalab.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostForm {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotBlank(message = "작성자를 입력해주세요.")
    @Size(max = 50, message = "작성자는 50자 이하여야 합니다.")
    private String author;

    public PostForm() {
    }

    public static PostForm from(Post post) {
        PostForm form = new PostForm();
        form.title = post.getTitle();
        form.content = post.getContent();
        form.author = post.getAuthor();
        return form;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
```

- [ ] **Step 4: 폼 검증 테스트 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostFormTest"
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 5: 입력 폼 커밋**

```powershell
git add src/main/java/com/study/jpalab/post/PostForm.java src/test/java/com/study/jpalab/post/PostFormTest.java
git diff --cached --check
git commit -m "게시글 입력 검증 폼 추가"
```

---

### Task 4: Service 목록·상세·등록

**Files:**

- Create: `src/test/java/com/study/jpalab/post/PostServiceTest.java`
- Create: `src/main/java/com/study/jpalab/post/PostService.java`
- Create: `src/main/java/com/study/jpalab/post/PostNotFoundException.java`

**Interfaces:**

- Consumes: `PostRepository`, `Post`, `PostForm`
- Produces: `findAll()`, `findById(Long)`, `create(PostForm)`

- [ ] **Step 1: 목록·상세·등록 통합 테스트 작성**

`src/test/java/com/study/jpalab/post/PostServiceTest.java`:

```java
package com.study.jpalab.post;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void createAndFindPost() {
        Post created = postService.create(form("등록 제목", "등록 내용", "student"));
        entityManager.flush();
        entityManager.clear();

        Post found = postService.findById(created.getId());

        assertThat(found.getTitle()).isEqualTo("등록 제목");
        assertThat(found.getContent()).isEqualTo("등록 내용");
        assertThat(found.getAuthor()).isEqualTo("student");
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getUpdatedAt()).isNotNull();
    }

    @Test
    void findAllPostsInIdDescendingOrder() {
        Post created = postService.create(form("최신 글", "내용", "student"));

        var posts = postService.findAll();

        assertThat(posts).isNotEmpty();
        assertThat(posts.getFirst().getId()).isEqualTo(created.getId());
    }

    private PostForm form(String title, String content, String author) {
        PostForm form = new PostForm();
        form.setTitle(title);
        form.setContent(content);
        form.setAuthor(author);
        return form;
    }
}
```

- [ ] **Step 2: 테스트 실패 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostServiceTest"
```

Expected: compilation FAIL because `PostService` does not exist.

- [ ] **Step 3: 404 예외 구현**

`src/main/java/com/study/jpalab/post/PostNotFoundException.java`:

```java
package com.study.jpalab.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("게시글을 찾을 수 없습니다. id=" + id);
    }
}
```

- [ ] **Step 4: 읽기와 등록 Service 구현**

`src/main/java/com/study/jpalab/post/PostService.java`:

```java
package com.study.jpalab.post;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @Transactional
    public Post create(PostForm form) {
        Post post = Post.create(
                form.getTitle(),
                form.getContent(),
                form.getAuthor()
        );
        return postRepository.save(post);
    }
}
```

- [ ] **Step 5: Service 통합 테스트 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostServiceTest"
```

Expected:

- `BUILD SUCCESSFUL`
- 테스트가 만든 게시글은 트랜잭션 롤백
- PostgreSQL 시퀀스 번호가 건너뛰어도 실패로 판단하지 않음

- [ ] **Step 6: 읽기와 등록 Service 커밋**

```powershell
git add src/main/java/com/study/jpalab/post/PostService.java src/main/java/com/study/jpalab/post/PostNotFoundException.java src/test/java/com/study/jpalab/post/PostServiceTest.java
git diff --cached --check
git commit -m "게시글 조회 등록 서비스 추가"
```

---

### Task 5: Service 수정·삭제와 변경 감지

**Files:**

- Modify: `src/test/java/com/study/jpalab/post/PostServiceTest.java`
- Modify: `src/main/java/com/study/jpalab/post/PostService.java`

**Interfaces:**

- Consumes: Task 4의 `PostService`
- Produces: `update(Long, PostForm)`, `delete(Long)`, 없는 ID의 `PostNotFoundException`

- [ ] **Step 1: 수정·삭제·없는 게시글 테스트를 `PostServiceTest`에 추가**

다음 메서드를 기존 테스트 클래스에 추가한다.

```java
    @Test
    void updatePostWithDirtyChecking() {
        Post created = postService.create(form("이전 제목", "이전 내용", "student"));
        entityManager.flush();
        var previousUpdatedAt = created.getUpdatedAt();

        postService.update(created.getId(), form("수정 제목", "수정 내용", "tester"));
        entityManager.flush();
        entityManager.clear();

        Post updated = postService.findById(created.getId());
        assertThat(updated.getTitle()).isEqualTo("수정 제목");
        assertThat(updated.getContent()).isEqualTo("수정 내용");
        assertThat(updated.getAuthor()).isEqualTo("tester");
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    void deletePost() {
        Post created = postService.create(form("삭제 제목", "삭제 내용", "student"));
        entityManager.flush();

        postService.delete(created.getId());
        entityManager.flush();

        assertThat(postRepository.findById(created.getId())).isEmpty();
    }

    @Test
    void throwExceptionWhenPostDoesNotExist() {
        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> postService.findById(-1L)
                )
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("-1");
    }
```

- [ ] **Step 2: 테스트 실패 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostServiceTest"
```

Expected: compilation FAIL because `update` and `delete` do not exist.

- [ ] **Step 3: `PostService`에 수정과 삭제 구현**

`PostService`의 마지막 `}` 앞에 다음 메서드를 추가한다.

```java
    @Transactional
    public void update(Long id, PostForm form) {
        Post post = findById(id);
        post.update(
                form.getTitle(),
                form.getContent(),
                form.getAuthor()
        );
    }

    @Transactional
    public void delete(Long id) {
        Post post = findById(id);
        postRepository.delete(post);
    }
```

수정 메서드에서 `postRepository.save(post)`를 호출하지 않는다. 트랜잭션 종료 시 Hibernate 변경 감지가 UPDATE를 실행해야 한다.

- [ ] **Step 4: 수정·삭제 통합 테스트 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostServiceTest"
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 5: 변경 감지와 삭제 커밋**

```powershell
git add src/main/java/com/study/jpalab/post/PostService.java src/test/java/com/study/jpalab/post/PostServiceTest.java
git diff --cached --check
git commit -m "게시글 변경 감지 삭제 서비스 추가"
```

---

### Task 6: 목록·상세 Controller와 JSP

**Files:**

- Create: `src/test/java/com/study/jpalab/post/PostControllerTest.java`
- Create: `src/main/java/com/study/jpalab/post/PostController.java`
- Create: `src/main/webapp/WEB-INF/jsp/posts/list.jsp`
- Create: `src/main/webapp/WEB-INF/jsp/posts/detail.jsp`
- Create: `src/main/resources/static/css/board.css`

**Interfaces:**

- Consumes: `PostService.findAll()`, `PostService.findById(Long)`
- Produces: `GET /posts`, `GET /posts/{id}`, `posts/list`, `posts/detail`

- [ ] **Step 1: 목록·상세 Controller 테스트 작성**

`src/test/java/com/study/jpalab/post/PostControllerTest.java`:

```java
package com.study.jpalab.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    void showPostList() throws Exception {
        when(postService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    void showPostDetail() throws Exception {
        Post post = Post.create("제목", "내용", "작성자");
        when(postService.findById(1L)).thenReturn(post);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/detail"))
                .andExpect(model().attribute("post", post));
    }
}
```

- [ ] **Step 2: Controller 테스트 실패 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostControllerTest"
```

Expected: compilation FAIL because `PostController` does not exist.

- [ ] **Step 3: 읽기 Controller 구현**

`src/main/java/com/study/jpalab/post/PostController.java`:

```java
package com.study.jpalab.post;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findById(id));
        return "posts/detail";
    }
}
```

- [ ] **Step 4: 전통적인 표 목록 JSP 작성**

`src/main/webapp/WEB-INF/jsp/posts/list.jsp`:

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판</title>
    <c:url var="boardCss" value="/css/board.css"/>
    <link rel="stylesheet" href="${boardCss}">
</head>
<body>
<main class="board-container">
    <header class="board-header">
        <div>
            <h1>게시판</h1>
            <p>JPA로 만드는 첫 번째 게시판</p>
        </div>
    </header>

    <table class="post-table">
        <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="post" items="${posts}">
            <c:url var="detailUrl" value="/posts/${post.id}"/>
            <tr>
                <td><c:out value="${post.id}"/></td>
                <td><a href="${detailUrl}"><c:out value="${post.title}"/></a></td>
                <td><c:out value="${post.author}"/></td>
                <td><c:out value="${post.createdAt}"/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty posts}">
            <tr>
                <td class="empty" colspan="4">등록된 게시글이 없습니다.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</main>
</body>
</html>
```

- [ ] **Step 5: 상세 JSP 작성**

`src/main/webapp/WEB-INF/jsp/posts/detail.jsp`:

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 상세</title>
    <c:url var="boardCss" value="/css/board.css"/>
    <link rel="stylesheet" href="${boardCss}">
</head>
<body>
<main class="board-container">
    <header class="board-header">
        <div>
            <h1>게시글 상세</h1>
            <p>선택한 게시글의 전체 내용</p>
        </div>
    </header>

    <article class="post-detail">
        <h2><c:out value="${post.title}"/></h2>
        <div class="post-meta">
            <span>작성자 <c:out value="${post.author}"/></span>
            <span>작성 <c:out value="${post.createdAt}"/></span>
            <span>수정 <c:out value="${post.updatedAt}"/></span>
        </div>
        <div class="post-content"><c:out value="${post.content}"/></div>
    </article>

    <div class="actions">
        <c:url var="listUrl" value="/posts"/>
        <a class="button secondary" href="${listUrl}">목록</a>
    </div>
</main>
</body>
</html>
```

- [ ] **Step 6: 공통 CSS 작성**

`src/main/resources/static/css/board.css`:

```css
* {
    box-sizing: border-box;
}

body {
    margin: 0;
    background: #f4f6f9;
    color: #202737;
    font-family: Arial, "Noto Sans KR", sans-serif;
}

a {
    color: #28395f;
    text-decoration: none;
}

a:hover {
    text-decoration: underline;
}

.board-container {
    width: min(960px, calc(100% - 32px));
    margin: 64px auto;
    padding: 32px;
    border-radius: 14px;
    background: #ffffff;
    box-shadow: 0 12px 34px rgba(30, 42, 68, 0.08);
}

.board-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    margin-bottom: 24px;
}

.board-header h1 {
    margin: 0;
}

.board-header p {
    margin: 8px 0 0;
    color: #7a8496;
}

.post-table {
    width: 100%;
    border-collapse: collapse;
    border-top: 2px solid #30394f;
}

.post-table th,
.post-table td {
    padding: 14px 12px;
    border-bottom: 1px solid #e2e6ed;
    text-align: center;
}

.post-table th {
    background: #f7f8fa;
}

.post-table th:nth-child(2),
.post-table td:nth-child(2) {
    text-align: left;
}

.empty {
    padding: 48px;
    color: #8b93a3;
}

.button {
    display: inline-block;
    padding: 10px 16px;
    border: 1px solid transparent;
    border-radius: 7px;
    cursor: pointer;
    font-size: 14px;
}

.button.primary {
    background: #30394f;
    color: #ffffff;
}

.button.secondary {
    border-color: #cdd3de;
    background: #ffffff;
    color: #30394f;
}

.button.danger {
    border-color: #f0b8b8;
    background: #fff5f5;
    color: #b42323;
}

.post-detail {
    border-top: 2px solid #30394f;
}

.post-detail h2 {
    margin: 0;
    padding: 24px 4px 16px;
    border-bottom: 1px solid #e2e6ed;
}

.post-meta {
    display: flex;
    gap: 20px;
    padding: 14px 4px;
    border-bottom: 1px solid #e2e6ed;
    color: #7a8496;
    font-size: 14px;
}

.post-content {
    min-height: 240px;
    padding: 28px 4px;
    line-height: 1.8;
    white-space: pre-wrap;
}

.actions,
.form-actions {
    display: flex;
    justify-content: space-between;
    gap: 8px;
    margin-top: 24px;
}

@media (max-width: 640px) {
    .board-container {
        margin: 20px auto;
        padding: 20px;
    }

    .post-table th:nth-child(4),
    .post-table td:nth-child(4) {
        display: none;
    }

    .post-meta {
        flex-direction: column;
        gap: 6px;
    }
}
```

- [ ] **Step 7: Controller 테스트와 브라우저 조회 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostControllerTest"
.\gradlew.bat bootRun
```

Expected:

- Controller 테스트 `BUILD SUCCESSFUL`
- `http://localhost:8080/posts`에서 기존 게시글 3건 출력
- 제목을 누르면 상세 화면 표시
- 사용자 데이터가 HTML 태그로 실행되지 않음

`bootRun`은 브라우저 확인 후 `Ctrl+C`로 종료한다.

- [ ] **Step 8: 목록과 상세 화면 커밋**

```powershell
git add src/main/java/com/study/jpalab/post/PostController.java src/main/webapp/WEB-INF/jsp/posts/list.jsp src/main/webapp/WEB-INF/jsp/posts/detail.jsp src/main/resources/static/css/board.css src/test/java/com/study/jpalab/post/PostControllerTest.java
git diff --cached --check
git commit -m "JSP 게시글 목록 상세 화면 추가"
```

---

### Task 7: 게시글 등록 폼

**Files:**

- Modify: `src/test/java/com/study/jpalab/post/PostControllerTest.java`
- Modify: `src/main/java/com/study/jpalab/post/PostController.java`
- Modify: `src/main/webapp/WEB-INF/jsp/posts/list.jsp`
- Create: `src/main/webapp/WEB-INF/jsp/posts/form.jsp`
- Modify: `src/main/resources/static/css/board.css`

**Interfaces:**

- Consumes: `PostService.create(PostForm)`
- Produces: `GET /posts/new`, `POST /posts`, 검증 실패 폼, 등록 성공 상세 리다이렉트

- [ ] **Step 1: 등록 Controller 테스트를 기존 테스트 클래스에 추가**

정적 import를 추가한다.

```java
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
```

다음 테스트를 클래스에 추가한다.

```java
    @Test
    void showCreateForm() throws Exception {
        mockMvc.perform(get("/posts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/form"))
                .andExpect(model().attributeExists("postForm"))
                .andExpect(model().attribute("formAction", "/posts"));
    }

    @Test
    void createPostAndRedirectToDetail() throws Exception {
        Post savedPost = org.mockito.Mockito.mock(Post.class);
        when(savedPost.getId()).thenReturn(4L);
        when(postService.create(any(PostForm.class))).thenReturn(savedPost);

        mockMvc.perform(post("/posts")
                        .param("title", "등록 제목")
                        .param("content", "등록 내용")
                        .param("author", "student"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/4"));
    }

    @Test
    void rejectInvalidCreateForm() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("title", " ")
                        .param("content", " ")
                        .param("author", " "))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/form"))
                .andExpect(model().attributeHasFieldErrors(
                        "postForm", "title", "content", "author"
                ));

        verify(postService, never()).create(any(PostForm.class));
    }
```

- [ ] **Step 2: 등록 테스트 실패 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostControllerTest"
```

Expected: FAIL with 404 or view mismatch because 등록 엔드포인트가 없다.

- [ ] **Step 3: Controller에 등록 엔드포인트 구현**

다음 import를 `PostController`에 추가한다.

```java
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
```

다음 메서드를 클래스에 추가한다.

```java
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        configureForm(model, "게시글 작성", "/posts", "저장");
        return "posts/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("postForm") PostForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            configureForm(model, "게시글 작성", "/posts", "저장");
            return "posts/form";
        }

        Post post = postService.create(form);
        return "redirect:/posts/" + post.getId();
    }

    private void configureForm(
            Model model,
            String pageTitle,
            String formAction,
            String submitLabel
    ) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("formAction", formAction);
        model.addAttribute("submitLabel", submitLabel);
    }
```

- [ ] **Step 4: 목록 JSP에 글쓰기 링크 추가**

`list.jsp`의 `board-header` 안에서 제목을 감싼 `div` 다음에 아래 코드를 추가한다.

```jsp
        <c:url var="newPostUrl" value="/posts/new"/>
        <a class="button primary" href="${newPostUrl}">글쓰기</a>
```

- [ ] **Step 5: 등록·수정 공용 JSP 작성**

`src/main/webapp/WEB-INF/jsp/posts/form.jsp`:

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}"/></title>
    <c:url var="boardCss" value="/css/board.css"/>
    <link rel="stylesheet" href="${boardCss}">
</head>
<body>
<main class="board-container form-container">
    <header class="board-header">
        <div>
            <h1><c:out value="${pageTitle}"/></h1>
            <p>제목, 작성자와 내용을 입력해주세요.</p>
        </div>
    </header>

    <c:url var="actionUrl" value="${formAction}"/>
    <form:form method="post" action="${actionUrl}" modelAttribute="postForm">
        <div class="form-field">
            <form:label path="title">제목</form:label>
            <form:input path="title" maxlength="200"/>
            <form:errors path="title" cssClass="field-error"/>
        </div>

        <div class="form-field">
            <form:label path="author">작성자</form:label>
            <form:input path="author" maxlength="50"/>
            <form:errors path="author" cssClass="field-error"/>
        </div>

        <div class="form-field">
            <form:label path="content">내용</form:label>
            <form:textarea path="content" rows="12"/>
            <form:errors path="content" cssClass="field-error"/>
        </div>

        <div class="form-actions">
            <c:url var="listUrl" value="/posts"/>
            <a class="button secondary" href="${listUrl}">취소</a>
            <button class="button primary" type="submit">
                <c:out value="${submitLabel}"/>
            </button>
        </div>
    </form:form>
</main>
</body>
</html>
```

- [ ] **Step 6: 폼 CSS 추가**

`board.css` 끝에 다음 스타일을 추가한다.

```css
.form-container {
    max-width: 760px;
}

.form-field {
    margin-bottom: 20px;
}

.form-field label {
    display: block;
    margin-bottom: 8px;
    font-weight: 700;
}

.form-field input,
.form-field textarea {
    width: 100%;
    padding: 12px;
    border: 1px solid #cdd3de;
    border-radius: 7px;
    font: inherit;
}

.form-field textarea {
    resize: vertical;
}

.field-error {
    display: block;
    margin-top: 7px;
    color: #b42323;
    font-size: 13px;
}
```

- [ ] **Step 7: 등록 테스트와 브라우저 등록 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostControllerTest"
.\gradlew.bat bootRun
```

Browser:

1. `/posts/new`에서 빈 값 저장 시 세 필드 오류 확인
2. 별도 실습 게시글 등록
3. 등록 후 새 게시글 상세 화면으로 이동 확인
4. 생성된 게시글 ID 기록

- [ ] **Step 8: 게시글 등록 커밋**

```powershell
git add src/main/java/com/study/jpalab/post/PostController.java src/main/webapp/WEB-INF/jsp/posts/list.jsp src/main/webapp/WEB-INF/jsp/posts/form.jsp src/main/resources/static/css/board.css src/test/java/com/study/jpalab/post/PostControllerTest.java
git diff --cached --check
git commit -m "JSP 게시글 등록 폼 추가"
```

---

### Task 8: 게시글 수정·삭제와 404

**Files:**

- Modify: `src/test/java/com/study/jpalab/post/PostControllerTest.java`
- Modify: `src/main/java/com/study/jpalab/post/PostController.java`
- Modify: `src/main/webapp/WEB-INF/jsp/posts/detail.jsp`

**Interfaces:**

- Consumes: `PostService.update(Long, PostForm)`, `delete(Long)`, `findById(Long)`
- Produces: 수정 폼, 수정 POST, 삭제 POST, HTTP 404

- [ ] **Step 1: 수정·삭제·404 Controller 테스트 추가**

정적 import를 추가한다.

```java
import static org.mockito.Mockito.doThrow;
```

다음 테스트를 기존 `PostControllerTest`에 추가한다.

```java
    @Test
    void showEditForm() throws Exception {
        Post post = Post.create("기존 제목", "기존 내용", "student");
        when(postService.findById(1L)).thenReturn(post);

        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/form"))
                .andExpect(model().attributeExists("postForm"))
                .andExpect(model().attribute("formAction", "/posts/1/edit"));
    }

    @Test
    void updatePostAndRedirectToDetail() throws Exception {
        mockMvc.perform(post("/posts/1/edit")
                        .param("title", "수정 제목")
                        .param("content", "수정 내용")
                        .param("author", "tester"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postService).update(
                org.mockito.ArgumentMatchers.eq(1L),
                any(PostForm.class)
        );
    }

    @Test
    void rejectInvalidEditForm() throws Exception {
        mockMvc.perform(post("/posts/1/edit")
                        .param("title", " ")
                        .param("content", " ")
                        .param("author", " "))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/form"))
                .andExpect(model().attributeHasFieldErrors(
                        "postForm", "title", "content", "author"
                ));

        verify(postService, never()).update(
                org.mockito.ArgumentMatchers.eq(1L),
                any(PostForm.class)
        );
    }

    @Test
    void deletePostAndRedirectToList() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postService).delete(1L);
    }

    @Test
    void returnNotFoundWhenPostDoesNotExist() throws Exception {
        doThrow(new PostNotFoundException(-1L))
                .when(postService)
                .delete(-1L);

        mockMvc.perform(post("/posts/-1/delete"))
                .andExpect(status().isNotFound());
    }
```

- [ ] **Step 2: 수정·삭제 테스트 실패 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostControllerTest"
```

Expected: FAIL with 404 because 수정·삭제 엔드포인트가 없다.

- [ ] **Step 3: Controller에 수정·삭제 구현**

`PostController`의 마지막 `}` 앞, `configureForm` 메서드보다 위에 다음 메서드를 추가한다.

```java
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("postForm", PostForm.from(post));
        configureForm(
                model,
                "게시글 수정",
                "/posts/" + id + "/edit",
                "수정"
        );
        return "posts/form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("postForm") PostForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            configureForm(
                    model,
                    "게시글 수정",
                    "/posts/" + id + "/edit",
                    "수정"
            );
            return "posts/form";
        }

        postService.update(id, form);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }
```

- [ ] **Step 4: 상세 JSP에 수정과 삭제 동작 추가**

`detail.jsp`에서 기존 `actions` 블록을 다음 코드로 교체한다.

```jsp
    <div class="actions">
        <c:url var="listUrl" value="/posts"/>
        <a class="button secondary" href="${listUrl}">목록</a>

        <div>
            <c:url var="editUrl" value="/posts/${post.id}/edit"/>
            <c:url var="deleteUrl" value="/posts/${post.id}/delete"/>
            <a class="button secondary" href="${editUrl}">수정</a>
            <form action="${deleteUrl}"
                  method="post"
                  class="inline-form"
                  onsubmit="return confirm('게시글을 삭제할까요?');">
                <button class="button danger" type="submit">삭제</button>
            </form>
        </div>
    </div>
```

`board.css` 끝에 다음 스타일을 추가한다.

```css
.actions > div {
    display: flex;
    gap: 8px;
}

.inline-form {
    display: inline;
}
```

- [ ] **Step 5: 수정·삭제·404 테스트 확인**

Run:

```powershell
.\gradlew.bat test --tests "com.study.jpalab.post.PostControllerTest"
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 6: 브라우저에서 실습 게시글만 수정하고 삭제**

Run:

```powershell
.\gradlew.bat bootRun
```

Browser:

1. Task 7에서 생성한 실습 게시글 상세로 이동
2. 제목, 내용과 작성자를 수정
3. 상세 화면에 수정값과 변경된 수정일 표시 확인
4. 삭제 버튼을 눌러 확인창 표시 확인
5. 삭제 후 목록 이동 확인
6. 기존 게시글 3건이 남아 있는지 확인
7. `/posts/-1` 요청이 404인지 확인

- [ ] **Step 7: 수정·삭제 화면 커밋**

```powershell
git add src/main/java/com/study/jpalab/post/PostController.java src/main/webapp/WEB-INF/jsp/posts/detail.jsp src/main/resources/static/css/board.css src/test/java/com/study/jpalab/post/PostControllerTest.java
git diff --cached --check
git commit -m "JSP 게시글 수정 삭제 기능 추가"
```

---

### Task 9: 콘솔 Runner 제거, 전체 검증과 학습 문서 인계

**Files:**

- Delete: `src/main/java/com/study/jpalab/post/PostConsoleRunner.java`
- Modify: `README.md`
- Modify: `docs/learning/STATUS.md`
- Modify: `docs/learning/ROADMAP.md`
- Modify: `docs/learning/DECISIONS.md`

**Interfaces:**

- Consumes: Task 1~8의 완성된 JSP CRUD
- Produces: 깨끗한 애플리케이션 시작, 재현 가능한 War, 다음 PC가 읽을 학습 상태

- [ ] **Step 1: 웹 조회가 대체한 콘솔 Runner 제거**

`src/main/java/com/study/jpalab/post/PostConsoleRunner.java`를 삭제한다.

- [ ] **Step 2: 전체 자동 테스트 실행**

Run:

```powershell
.\gradlew.bat clean test
```

Expected:

- `BUILD SUCCESSFUL`
- 기존 `PostMappingTest`, `PostRepositoryTest` 성공
- 새 `PostTest`, `PostFormTest`, `PostServiceTest`, `PostControllerTest` 성공

- [ ] **Step 3: 실행형 War 생성과 실행 확인**

Run:

```powershell
.\gradlew.bat bootWar
java -jar build\libs\jpa-performance-lab-0.0.1-SNAPSHOT.war
```

Expected:

- `bootWar`가 `BUILD SUCCESSFUL`
- 외부 Tomcat 없이 포트 8080 시작
- `/actuator/health`의 전체 상태와 DB 상태가 `UP`
- `/posts`에서 기존 게시글 3건 출력
- Hibernate SELECT가 한 번만 출력

확인 후 `Ctrl+C`로 종료한다.

- [ ] **Step 4: README 기술 스택과 실행 명령 갱신**

`README.md`에서 다음 내용을 반영한다.

- View 기술을 Thymeleaf에서 JSP/JSTL로 변경
- Packaging을 실행형 War로 기록
- 애플리케이션 실행에 `bootRun`과 `java -jar build\libs\jpa-performance-lab-0.0.1-SNAPSHOT.war`를 모두 기록
- 게시판 URL `http://localhost:8080/posts` 추가
- 외부 Tomcat 설치가 필요 없음을 기록

- [ ] **Step 5: 기술 결정 기록 추가**

`docs/learning/DECISIONS.md` 끝에 다음 결정을 추가한다.

```markdown
## 2026-07-27 — JSP 게시판과 실행형 War

- 결정: Thymeleaf를 제거하고 JSP/JSTL 화면과 실행형 War 패키징을 사용한다.
- 이유: JPA 게시판 CRUD와 함께 JSP, JSTL, Spring MVC 폼 바인딩을 직접 학습하기 위해서다.
- 변경 조건: JSP 학습이 끝나고 운영 친화적인 템플릿 또는 분리된 프런트엔드가 필요하면 Thymeleaf나 REST API 기반 UI로 전환을 검토한다.
```

- [ ] **Step 6: 로드맵과 현재 상태 갱신**

검증이 모두 성공한 뒤 `ROADMAP.md`에서 다음 상태를 반영한다.

- 4단계 게시글 등록과 조회: 완료
- 5단계 게시글 수정과 삭제: 완료
- 6단계 이름을 `JSP 게시판 화면`으로 변경하고 완료
- 7단계 JPA 테스트: 완료
- 8단계 페이징과 검색: 진행 중

`STATUS.md`에는 다음 사실을 기록한다.

- 실행형 War와 JSP/JSTL 전환 완료
- `/posts` 목록·상세·등록·수정·삭제 완료
- 입력 검증과 없는 게시글 404 확인
- Service 통합 테스트와 Controller 테스트 성공
- 기존 게시글 3건 보존
- 다음 한 단계는 페이징을 적용하지 않은 현재 목록 SELECT와 화면 동작을 기준선으로 기록하는 것
- JSP는 실행형 Jar가 아닌 War가 필요하다는 알려진 제약

- [ ] **Step 7: 변경 범위와 비밀정보 확인**

Run:

```powershell
git status --short
git diff --check
git diff --stat
git log -5 --oneline
```

Expected:

- `jpa-performance-lab` 관련 파일만 변경
- DB username과 password가 diff나 커밋 메시지에 새로 노출되지 않음
- 다른 ODoc 프로젝트 변경을 스테이징하지 않음

- [ ] **Step 8: 최종 학습 단계 커밋**

```powershell
git add README.md docs/learning/STATUS.md docs/learning/ROADMAP.md docs/learning/DECISIONS.md src/main/java/com/study/jpalab/post/PostConsoleRunner.java
git diff --cached --check
git commit -m "JSP 게시판 CRUD 학습 완료"
```

`git push origin main`은 사용자가 현재 진행 내용을 Git에 올리거나 저장하라고 명시적으로 요청한 경우에만 실행한다.
