# JPA 누적 교재형 커리큘럼 Word 문서 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 현재 프로젝트의 실제 학습 상태를 반영한 교재형 `docs/learning/JPA_학습_커리큘럼.docx`를 만들고, 이후 AI가 Markdown 기준 데이터와 함께 안전하게 갱신할 수 있는 규칙을 프로젝트 문서에 연결한다.

**Architecture:** `STATUS.md`, `ROADMAP.md`, `DECISIONS.md`, 실제 코드와 Git 이력을 입력으로 삼아 임시 Python 빌더가 단일 DOCX를 생성한다. 최종 DOCX만 Git에 추적하고 임시 빌더·렌더 결과는 `build/docx-curriculum/`에 둔다. `compact_reference_guide` 프리셋과 한국어 글꼴 오버라이드를 적용하고, 구조 감사와 전 페이지 PNG 검토를 통과한 파일만 전달한다.

**Tech Stack:** Bundled Python, python-docx, OOXML, LibreOffice renderer, Poppler, `compact_reference_guide` design preset

## Global Constraints

- 최종 파일 경로는 `docs/learning/JPA_학습_커리큘럼.docx`다.
- 한 개의 Word 파일에 장을 계속 누적한다.
- 실제 코드와 테스트, `STATUS.md`, `ROADMAP.md`, `DECISIONS.md`가 Word보다 우선한다.
- 현재 기준은 1~4단계 완료, 5단계 수정·삭제 진행 중, 6단계 JSP 목록·상세·등록 완료다.
- 아직 시작하지 않은 단원은 학습 목표·핵심 개념·실습 예정·완료 조건까지만 작성한다.
- DB 사용자명과 비밀번호를 DOCX, README, AGENTS에 기록하지 않는다.
- 공유 Supabase 프로젝트의 부하 테스트는 Smoke·Baseline으로 제한하고 Stress·Spike·Soak는 격리 환경에서만 계획한다.
- 디자인 프리셋은 `compact_reference_guide` 하나만 사용한다.
- 한국어 글리프 안정성을 위해 동아시아 글꼴은 `맑은 고딕`, 영문·숫자 본문은 Calibri, 코드·명령어는 Consolas를 사용한다.
- DOCX를 렌더링하고 모든 페이지를 시각적으로 확인하기 전에는 완료로 표시하지 않는다.
- AI가 만드는 커밋 메시지는 한글로 작성한다.
- 사용자의 명시적인 요청 전에는 원격 저장소에 푸시하지 않는다.

---

## File Structure

### 생성 파일

- `docs/learning/JPA_학습_커리큘럼.docx`: 사용자에게 전달하고 Git으로 추적하는 단일 누적 교재
- `build/docx-curriculum/build_jpa_curriculum.py`: 생성 중에만 사용하는 임시 DOCX 빌더
- `build/docx-curriculum/render/page-<N>.png`: 시각 검토용 임시 렌더 결과

### 변경 파일

- `README.md`: Word 커리큘럼의 역할과 갱신 요청 방법 안내
- `AGENTS.md`: AI가 커리큘럼 갱신 요청을 처리하는 순서와 렌더링 의무
- `docs/learning/STATUS.md`: 교재 생성 완료 사실과 현재 JPA 다음 단계 유지

### 참조 파일

- `docs/learning/STATUS.md`
- `docs/learning/ROADMAP.md`
- `docs/learning/DECISIONS.md`
- `docs/superpowers/specs/2026-07-28-jpa-curriculum-docx-design.md`
- `src/main/java/com/study/jpalab/post/**`
- `src/main/webapp/WEB-INF/jsp/posts/**`
- `src/main/resources/application.yaml`
- `build.gradle`

---

### Task 1: 누적 교재 DOCX 생성과 구조 검증

**Files:**

- Create: `build/docx-curriculum/build_jpa_curriculum.py`
- Create: `docs/learning/JPA_학습_커리큘럼.docx`
- Read: `docs/learning/STATUS.md`
- Read: `docs/learning/ROADMAP.md`
- Read: `docs/learning/DECISIONS.md`

**Interfaces:**

- Consumes: Markdown 기준 데이터, 실제 프로젝트 기술 스택과 완료 상태
- Produces: Word에서 읽을 수 있는 단일 누적 교재 `JPA_학습_커리큘럼.docx`

- [ ] **Step 1: 문서 런타임과 출력 폴더 확인**

다음 번들 런타임만 사용한다.

```powershell
$docPython = 'C:\Users\user\.cache\codex-runtimes\codex-primary-runtime\dependencies\python\python.exe'
$docSkill = 'C:\Users\user\.codex\plugins\cache\openai-primary-runtime\documents\26.727.11326\skills\documents'
$docBuild = 'C:\sw\ODOC\jpa-performance-lab\build\docx-curriculum'
$docOutput = 'C:\sw\ODOC\jpa-performance-lab\docs\learning\JPA_학습_커리큘럼.docx'

& $docPython -c "import docx; print(docx.__version__)"
```

Expected: python-docx 버전이 출력되고 import 오류가 없다.

- [ ] **Step 2: 기준 데이터와 실제 파일 존재 여부 확인**

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
rg -n "현재 학습 단계|다음에 할 한 단계|완료된 작업" docs/learning/STATUS.md
rg -n "완료|진행 중|대기" docs/learning/ROADMAP.md
rg -n "JSP 게시판|P6Spy|Gradle bootRun UTF-8" docs/learning/DECISIONS.md
rg --files src/main/java/com/study/jpalab/post src/main/webapp/WEB-INF/jsp/posts
```

Expected:

- 현재 단계는 `5단계 — 게시글 수정과 삭제`
- 다음 한 단계는 `PostService`와 `PostServiceImpl`의 수정 메서드
- JSP 목록·상세·등록 파일이 존재
- 기술 결정에 JSP, P6Spy, UTF-8 실행 방식이 존재

- [ ] **Step 3: 문서 스타일 토큰을 빌더에 고정**

`build_jpa_curriculum.py`에 다음 토큰 맵을 정의한다.

```python
TOKENS = {
    "page_width_in": 8.5,
    "page_height_in": 11.0,
    "margin_in": 1.0,
    "header_footer_distance_in": 0.492,
    "content_width_dxa": 9360,
    "body_ascii_font": "Calibri",
    "body_east_asia_font": "맑은 고딕",
    "body_size_pt": 11,
    "body_after_pt": 6,
    "body_line_spacing": 1.25,
    "title_size_pt": 28,
    "title_color": "0B2545",
    "h1": {"size": 16, "color": "2E74B5", "before": 18, "after": 10},
    "h2": {"size": 13, "color": "2E74B5", "before": 14, "after": 7},
    "h3": {"size": 12, "color": "1F4D78", "before": 10, "after": 5},
    "list_left_dxa": 540,
    "list_hanging_dxa": 270,
    "list_after_pt": 4,
    "table_width_dxa": 9360,
    "table_indent_dxa": 120,
    "cell_margins_dxa": {"top": 80, "bottom": 80, "start": 120, "end": 120},
    "table_header_fill": "E8EEF5",
    "callout_fill": "F4F6F9",
    "warning_fill": "FFF8E8",
    "muted_color": "5F6B7A",
}
```

Named overrides:

- `KoreanText`: 동아시아 글꼴 `맑은 고딕`
- `CodeBlock`: Consolas/맑은 고딕 9.5pt, `F4F6F9` 음영, 0.15in 좌우 들여쓰기
- `StatusComplete`: `E7F4EC` 채우기
- `StatusInProgress`: `FFF4D6` 채우기
- `StatusPending`: `F2F4F7` 채우기

- [ ] **Step 4: Word-native 구성요소 헬퍼 구현**

빌더에 다음 함수와 정확한 책임을 구현한다.

```python
def set_run_font(run, ascii_font, east_asia_font, size_pt, *, bold=False, color=None):
    """ASCII/HAnsi/EastAsia 글꼴, 크기, 굵기와 색을 한 번에 지정한다."""

def configure_styles(document):
    """Normal, Heading 1~3, Subtitle, Caption 스타일에 TOKENS 값을 적용한다."""

def add_numbering_definitions(document):
    """540 DXA 들여쓰기와 270 DXA hanging을 가진 실제 bullet/decimal numId를 반환한다."""

def add_list_item(document, text, num_id, *, level=0):
    """가짜 문자 bullet 없이 w:numPr을 사용하는 목록 항목을 만든다."""

def add_code_block(document, text):
    """CodeBlock named override로 코드·SQL·PowerShell 예시를 만든다."""

def add_callout(document, label, text, *, warning=False):
    """핵심 요약 또는 주의사항을 음영 문단으로 만든다."""

def add_fixed_table(document, headers, rows, widths_dxa, *, status_column=None):
    """행을 모두 만든 뒤 apply_table_geometry로 고정 DXA 표를 완성한다."""

def set_running_header_footer(document, updated_date):
    """첫 페이지를 제외한 머리글에 문서명, 바닥글에 갱신일과 PAGE 필드를 넣는다."""

def keep_with_next(paragraph):
    """제목과 다음 내용이 페이지 사이에서 분리되지 않게 한다."""
```

표는 다음 스킬 헬퍼를 import해 모든 행 생성 후 적용한다.

```python
import sys
sys.path.insert(
    0,
    r"C:\Users\user\.codex\plugins\cache\openai-primary-runtime"
    r"\documents\26.727.11326\skills\documents\scripts",
)
from table_geometry import apply_table_geometry
```

- [ ] **Step 5: 첫 페이지와 사용 방법 작성**

첫 페이지는 `editorial_cover` 패턴을 절제해 적용한다.

```text
JPA Performance Lab
JPA 학습 커리큘럼

Spring Boot 게시판 CRUD부터 쿼리·인덱스 개선과 부하 테스트까지

현재 단계  5단계 — 게시글 수정과 삭제
기준일      2026-07-28
관리 방식  Markdown 기준 데이터 + 단일 누적 Word 교재
```

다음 페이지에 `이 문서를 사용하는 방법`을 작성한다.

- `STATUS.md`: 현재 단계와 다음 한 단계
- `ROADMAP.md`: 전체 순서와 상태
- `DECISIONS.md`: 기술 선택 이유
- Word: 개념 설명, 실습 흐름, 오류와 확인 결과
- AI는 코드와 Markdown 확인 → 추가 제안 → 사용자 승인 → Word 렌더 검증 순서로 갱신

- [ ] **Step 6: 현재 프로젝트와 진행률 장 작성**

`1. 프로젝트와 학습 환경`에 기술 스택과 실행 방식을 설명한다. 실제 접속 계정은 기록하지 않는다.

`2. 전체 커리큘럼과 현재 진행률`에는 11단계 표를 만든다.

| 단계 | 상태 | 학습 결과 |
|---:|---|---|
| 1 | 완료 | 프로젝트 생성 및 Supabase PostgreSQL 연결 |
| 2 | 완료 | `Post` 엔티티와 `jpa_study.posts` 매핑 |
| 3 | 완료 | Spring Data JPA Repository |
| 4 | 완료 | 게시글 등록과 조회 |
| 5 | 진행 중 | 게시글 수정과 삭제 |
| 6 | 진행 중 | JSP 목록·상세·등록 완료 |
| 7 | 대기 | JPA 테스트 |
| 8 | 대기 | 페이징과 검색 |
| 9 | 대기 | Actuator 지표 관측 |
| 10 | 대기 | k6 Smoke·Baseline |
| 11 | 대기 | 쿼리·인덱스 개선 전후 비교 |

상태 열에는 `StatusComplete`, `StatusInProgress`, `StatusPending` 색을 적용한다.

- [ ] **Step 7: 완료한 학습 내용을 교재형으로 작성**

각 완료 단원은 `배운 이유 → 핵심 개념 → 프로젝트 적용 → 확인 방법 → 기억할 점` 순서를 사용한다.

초기 상세 단원:

1. Supabase `jpa_study` 스키마와 `posts` 테이블
2. Spring Boot·Gradle·PostgreSQL 연결
3. `Post` 엔티티와 기본 키·시간 컬럼 매핑
4. `PostRepository`와 `findAll()`
5. Controller·Service 인터페이스·구현체·Repository 계층
6. JSP 목록·상세·등록과 PRG 패턴
7. 엔티티 `create()`와 `update()` 행위 메서드
8. P6Spy 바인딩 값 포함 SQL과 `?`의 의미
9. Gradle `bootRun` UTF-8과 동적 접속 주소
10. Git과 Markdown을 이용한 PC 간 학습 인계

코드·SQL 예시는 현재 프로젝트에 존재하는 시그니처와 다음 형태만 사용한다.

```java
Post post = Post.create(title, content, author);
post.update(title, content, author);
```

```sql
select *
from jpa_study.posts
order by id;
```

```powershell
.\gradlew.bat test
.\gradlew.bat bootRun
```

- [ ] **Step 8: 현재 단원과 이후 단원 작성**

`5단계 — 게시글 수정과 삭제`는 다음 내용을 자세히 작성한다.

- 영속성 컨텍스트와 관리 상태
- 트랜잭션 안에서 엔티티 조회
- 엔티티 `update()` 호출
- 변경 감지 시점과 `UPDATE`
- 수정 폼, 상세 리다이렉트, P6Spy SQL
- 삭제는 새로 만든 실습 데이터에만 수행
- 완료 조건 5개

아직 시작하지 않은 단원은 다음 네 필드만 쓴다.

```text
학습 목표
핵심 개념
실습 예정
완료 조건
```

대상 단원:

- Validation 완성
- JPA 테스트
- 트랜잭션과 영속성 컨텍스트 심화
- 페이징·정렬·검색
- 연관관계와 N+1
- Query Method·JPQL·Native Query
- 인덱스와 실행 계획
- Actuator 지표 관측
- k6 부하 테스트
- 성능 개선 전후 비교

- [ ] **Step 9: 부록과 변경 이력 작성**

부록은 다음 순서로 만든다.

1. 실행·테스트 명령
2. 주소와 Health 확인
3. 자주 발생한 오류와 원인
4. JPA 핵심 용어
5. AI 커리큘럼 갱신 절차
6. 변경 이력

최초 변경 이력:

```text
2026-07-28 | 최초 작성 | 1~4단계 완료, 5단계 진행 중 상태를 교재에 반영
```

- [ ] **Step 10: DOCX 저장과 구조 감사**

```powershell
& $docPython "$docBuild\build_jpa_curriculum.py"
& $docPython "$docSkill\scripts\table_geometry.py" "$docOutput"
```

Expected:

- `docs/learning/JPA_학습_커리큘럼.docx` 생성
- 표 geometry issue `0`
- 문서 내부에 미완성 표식이나 실제 DB 사용자명·비밀번호 없음
- 제목 계층, 실제 번호 목록, 머리글·바닥글과 PAGE 필드 존재

- [ ] **Step 11: DOCX를 PNG로 렌더링**

```powershell
& $docPython "$docSkill\render_docx.py" `
    "$docOutput" `
    --output_dir "$docBuild\render" `
    --emit_pdf
```

Expected:

- `page-1.png`부터 마지막 페이지까지 연속 생성
- PDF가 비어 있지 않음
- 렌더링 오류가 없거나, PNG와 PDF가 정상 생성되는 LibreOffice 비치명 경고만 존재

- [ ] **Step 12: 모든 페이지를 100%로 시각 검토하고 수정 반복**

각 `page-<N>.png`를 모두 열어 다음을 확인한다.

- 한글과 코드 글꼴이 깨지지 않음
- 제목·본문·목록·표가 겹치거나 잘리지 않음
- 표 셀 텍스트가 경계에 붙지 않음
- 제목만 페이지 끝에 고립되지 않음
- 과도한 빈 페이지나 큰 공백이 없음
- 머리글·바닥글과 페이지 번호 위치가 일관됨

결함이 있으면 빌더를 수정하고 Step 10~12를 다시 실행한다.

- [ ] **Step 13: Word 교재 커밋**

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/docs/learning/JPA_학습_커리큘럼.docx
git diff --cached --check
git commit -m "JPA 누적 교재형 커리큘럼 작성"
```

---

### Task 2: AI 갱신 규칙과 학습 상태 연결

**Files:**

- Modify: `README.md`
- Modify: `AGENTS.md`
- Modify: `docs/learning/STATUS.md`

**Interfaces:**

- Consumes: Task 1에서 렌더 검증한 `JPA_학습_커리큘럼.docx`
- Produces: Codex와 Claude Code가 Markdown과 Word를 함께 유지하는 프로젝트 운영 규칙

- [ ] **Step 1: README에 Word 교재 역할 추가**

`현재 학습 상태 확인` 절에 다음 항목을 추가한다.

```markdown
- 교재형 개념·실습 기록: `docs/learning/JPA_학습_커리큘럼.docx`
```

다음 운영 설명을 추가한다.

```markdown
`STATUS.md`, `ROADMAP.md`, `DECISIONS.md`가 상태의 기준이며 Word 커리큘럼은 사람이 읽기 쉬운 누적 교재다. 새 학습 내용이나 순서 변경은 AI가 추가안을 먼저 제시하고 사용자가 승인하면 Markdown과 Word에 함께 반영한다.
```

- [ ] **Step 2: AGENTS에 커리큘럼 갱신 절차 추가**

`학습 방식`에 다음 규칙을 추가한다.

```markdown
- 사용자가 `커리큘럼에 추가할 내용 알려줘`, `오늘 진행한 내용을 커리큘럼에 반영해`, `Word 커리큘럼 갱신해`라고 말하면 실제 코드·테스트·Git과 `STATUS.md`, `ROADMAP.md`, `DECISIONS.md`를 먼저 확인한다.
- AI는 Word에 추가할 내용을 먼저 제안하고 사용자가 승인한 범위만 반영한다.
- 상태가 바뀌면 Markdown과 `docs/learning/JPA_학습_커리큘럼.docx`를 함께 갱신한다.
- DOCX 수정은 documents skill의 렌더링 절차를 사용하고 모든 페이지를 확인한다.
- 실제 DB 접속정보는 Word에 기록하지 않는다.
```

세션 시작 읽기 순서는 Markdown 3개까지 유지한다. Word 전체 교재는 커리큘럼 확인·추가·갱신 요청이 있을 때 읽는다.

- [ ] **Step 3: STATUS에 교재 생성 완료 기록**

`완료된 작업`에 다음 내용을 추가한다.

```markdown
- 실제 코드와 학습 Markdown을 기준으로 하는 단일 누적 Word 교재 `JPA_학습_커리큘럼.docx`를 만들고 AI 갱신 규칙을 연결했다.
```

현재 단계와 다음 한 단계는 변경하지 않는다.

- [ ] **Step 4: 문서 일관성과 보안 확인**

```powershell
Set-Location C:\sw\ODOC\jpa-performance-lab
rg -n "JPA_학습_커리큘럼|커리큘럼.*갱신|documents skill" README.md AGENTS.md docs/learning/STATUS.md
rg -n "현재 학습 단계|다음에 할 한 단계" docs/learning/STATUS.md
git diff --check
git status --short
```

Expected:

- 세 파일에서 Word 교재와 갱신 규칙이 연결됨
- 현재 단계는 게시글 수정·삭제로 유지
- 다음 한 단계는 Service 수정 메서드로 유지
- 새 문서나 diff에 실제 DB 접속정보가 없음

- [ ] **Step 5: 운영 문서 커밋**

```powershell
Set-Location C:\sw\ODOC
git add -- jpa-performance-lab/README.md jpa-performance-lab/AGENTS.md jpa-performance-lab/docs/learning/STATUS.md
git diff --cached --check
git commit -m "AI 커리큘럼 갱신 규칙 연결"
```

원격 푸시는 사용자가 현재 진행 내용을 Git에 올리라고 명시적으로 요청한 경우에만 실행한다.
