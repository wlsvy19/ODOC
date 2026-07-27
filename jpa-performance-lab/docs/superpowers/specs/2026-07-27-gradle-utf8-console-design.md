# Gradle bootRun UTF-8 콘솔 설계

## 목표

어느 PC에서든 ODoc 저장소를 `git pull`한 뒤 IntelliJ IDEA Community의 Gradle `bootRun`으로 애플리케이션을 실행하면 한글 로그가 깨지지 않게 한다.

## 현재 문제

IntelliJ의 개인 VM 옵션과 `.idea` 설정은 Git으로 공유되지 않는다. 새 PC마다 같은 옵션을 직접 입력하는 방식은 PC 간 학습 인계 목표와 맞지 않는다.

Java 26은 기본 문자 집합으로 UTF-8을 사용하지만 표준 출력과 표준 오류의 인코딩은 실행 환경에 따라 별도로 결정될 수 있다. 또한 `bootRun`을 실행·출력하는 Gradle 데몬 자체의 인코딩은 `bootRun` 자식 JVM 설정만으로 바뀌지 않는다. 따라서 프로젝트가 추적하는 Gradle 데몬과 애플리케이션 JVM 양쪽에 UTF-8을 명시한다.

## 결정

프로젝트의 `gradle.properties`와 `build.gradle`에서 다음 범위에 UTF-8을 명시한다.

- Gradle 데몬 JVM의 기본 문자 집합, 표준 출력 및 표준 오류 인코딩
- 모든 `JavaCompile` 작업의 소스 인코딩
- `bootRun` 애플리케이션 JVM의 기본 문자 집합
- `bootRun` 애플리케이션 JVM의 표준 출력 인코딩
- `bootRun` 애플리케이션 JVM의 표준 오류 인코딩
- Gradle `test` JVM의 동일한 세 인코딩

IntelliJ의 `.idea` 파일이나 PC별 Run Configuration은 Git에 추가하지 않는다.

## 실행 방식

IntelliJ IDEA Community에서 Gradle 도구 창의 다음 작업을 실행한다.

```text
Tasks → application → bootRun
```

PowerShell에서는 다음 명령이 같은 설정을 사용한다.

```powershell
.\gradlew.bat bootRun
```

`JpaPerformanceLabApplication` 옆의 초록색 버튼으로 직접 실행하는 방식은 이번 설정의 보장 범위에 포함하지 않는다.

## 설정 형태

`gradle.properties`에 Gradle 데몬 JVM 옵션을 저장한다. 메모리 한도는 Gradle의 기본값을 유지한다.

```properties
org.gradle.jvmargs=-Xmx512m -XX:MaxMetaspaceSize=384m -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8
```

`build.gradle`에 다음 구성을 추가한다.

```groovy
tasks.withType(JavaCompile).configureEach {
	options.encoding = 'UTF-8'
}

tasks.named('bootRun') {
	systemProperty 'file.encoding', 'UTF-8'
	systemProperty 'stdout.encoding', 'UTF-8'
	systemProperty 'stderr.encoding', 'UTF-8'
}

tasks.named('test') {
	useJUnitPlatform()
	systemProperty 'file.encoding', 'UTF-8'
	systemProperty 'stdout.encoding', 'UTF-8'
	systemProperty 'stderr.encoding', 'UTF-8'
}
```

기존 `tasks.named('test')` 블록은 새 블록을 중복 생성하지 않고 위 내용으로 확장한다.

## 검증

1. `.\gradlew.bat --stop` 후 Gradle 데몬이 `-Dfile.encoding=UTF-8`, `-Dstdout.encoding=UTF-8`, `-Dstderr.encoding=UTF-8`로 시작되는지 확인한다.
2. `.\gradlew.bat test`가 성공하는지 확인한다.
3. `.\gradlew.bat bootRun --info --args="--server.port=0"`으로 애플리케이션을 실행하고, 자식 JVM 인수와 `PostConsoleRunner` 한글 출력이 모두 정상인지 확인한다.
4. `git diff --check`로 공백 오류를 확인한다.

## 문서화

- README에 IntelliJ Community에서 Gradle `bootRun`을 실행하는 경로를 기록한다.
- `docs/learning/DECISIONS.md`에 PC 간 콘솔 UTF-8 결정을 기록한다.
- 검증이 성공하면 `docs/learning/STATUS.md`에 결과를 기록한다.

## 제외 범위

- IntelliJ 개인 설정과 `.idea` 파일 공유
- 운영 환경의 JVM 옵션
- Gradle `bootRun`이 아닌 IntelliJ Application 직접 실행
- 운영체제 전체 콘솔 코드 페이지 변경
