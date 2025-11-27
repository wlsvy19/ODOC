# 광안대교 통행료 수납 시스템 - 영업소 운영단말 시스템 Front-End
Front-End는 Back-End로부터 값을 읽어와서 사용자 화면에 표출하고 사용자의 조작에 따라 값을 삽입·수정·삭제하는 것을 Back-End에 요청하는 역할을 합니다.

## 주소
- 운영: http://10.66.1.31:9081/
- 개발: http://10.100.10.148:9081/

## 개발환경
개발환경과 운영환경은 동일하게 설정합니다.

- Vue 3.4.15
- Node.js 18.18.2
- Visual Studio Code 1.89.1
- Oracle Database 19c (19.3.0.0.0)
- OZReport 8.0.0101.20210127
- Ubuntu 22.04

## 주요 디렉토리

### 운영 서버
- /home/gdtcs/gdtcs_frontend/

### 개발 서버
- /home/gdtcs/gdtcs_frontend/

## 배포 서버에 따라 수정해야 하는 파일 목록
- server.js
- shutdown.sh
- startup.sh
- package.json
- package-lock.json
    
## 로그 경로

### 운영 서버
- /home/gdtcs/gdtcs_frontend/logs

### 개발 서버
- /home/gdtcs/gdtcs_frontend/logs

## 자동 로그 삭제
자동 로그 삭제를 위한 쉘 스크립트는 매일 23시 59분 실행되며 운영 환경에 설정되어 있습니다. 이 스크립트들은 자동 실행 스케줄러(Crontab)에 등록되어 있습니다.

### 운영 서버
- /home/gdtcs/delete_log.sh * 작성 중...

### 개발 서버
- /home/gdtcs/delete_log.sh * 작성 중...

## 실행 및 디버깅

### 운영
root 계정으로 로그인합니다.
- 서버 시작: systemctl start nodejs_gdtcs
- 서버 중지: systemctl stop nodejs_gdtcs
- 서버 로그 확인: tail -f /home/gdtcs/gdtcs_op_frontend/logs/access.log

### 개발
root 계정으로 로그인합니다.
- 서버 시작: systemctl start nodejs_gdtcs
- 서버 중지: systemctl stop nodejs_gdtcs
- 서버 로그 확인: tail -f /home/gdtcs/gdtcs_op_frontend/logs/access.log

## Customize configuration

See [Vite Configuration Reference](https://vitejs.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```

### vue 스타일 가이드: ESLint와 Prettier 사용
1. package.json 에서 devDependencies 영역에 관련 라이브러리 설치

1) "@rushstack/eslint-patch": "^1.1.0"
2) "@vue/eslint-config-prettier": "^7.0.0"
3) "eslint": "^8.5.0"
4) "eslint-plugin-vue": "^9.22.0"
5) "prettier": "^2.5.1"

2. vscode에서 eslint 플러그인 설치(prettier플러그인은 충돌때문에 설치x)

3. vscode에서 저장누르면 eslint 자동 적용되서 저장 설정
File - Preference - Settings - lint 검색 - 작업영역(workspace) - 하단으로 스크롤 Eslint: Validate - 아래코드추가


자동저장 설정
```sh
{
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": "nerver" // 자동저장 nerver: 비활성화, always:활성화
  },
  "eslint.validate": [
    "javascript",
    "vue"
  ],
}
```


한번에 lint 를 모두 적용
```sh
npm run lint
```

4. .eslintrc.cjs 설정파일
extends영역에는 공식문서에 나와있는 설정 적용

1) "plugin:vue/vue3-essential"
https://ko.vuejs.org/style-guide/ -> 스타일가이드와 우측에 ON THIS PAGE 참고

2) "eslint:recommended"
https://eslint.org/docs/rules -> 체크된 항목 자동으로 검사

3) "@vue/eslint-config-prettier"
https://prettier.io/docs/en/options -> 프리티어 doc



5. 설정에서 format on save 검색
Notebook › Format On Save: Enabled - 체크x
