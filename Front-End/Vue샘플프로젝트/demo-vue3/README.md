# demo-vue3

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur) + [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin).

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

### vue 스타일 가이드
0. eslint 플러그인 설치

.eslintrc.cjs 파일
1. "plugin:vue/vue3-essential"
https://ko.vuejs.org/style-guide/
스타일가이드와 우측에 ON THIS PAGE 참고

2. "eslint:recommended"
https://eslint.org/docs/rules 체크된 항목 자동으로 검사

3. "@vue/eslint-config-prettier"
https://prettier.io/docs/en/options 프리티어 doc

4. vscode에서 저장누르면 eslint 자동 적용되서 저장
edit - Preference - Settings - lint 검색 - 작업영역(workspace) -  
하단으로 스크롤 Eslint: Validate - 아래코드추가

```sh
{
    // 기존 설정이 있다면 그 아래에 추가
    "editor.codeActionsOnSave": {
        "source.fixAll.eslint": "explicit"
    },
    "eslint.validate": [
        

        "javascript",
        "javascriptreact",
        "vue",
        // 프로젝트에 맞는 다른 언어나 프레임워크 추가
    ],
}
```
모두 적용 하려면 
```sh
npm run lint
```

5. tab size 2로 설정

6. prettier 플러그인은 설치x

7. 설정에서 format on save 검색
Notebook › Format On Save: Enabled    - 체크x