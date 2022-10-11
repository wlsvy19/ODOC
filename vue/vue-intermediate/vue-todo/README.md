# vue-todo

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


vue-todo 프로젝트 리팩토링
초기 App.vue 에는 컴포넌트 등록만 해놨는데 메서드와 data가 많이 추가됨
각각 하위 컴포넌트에서 각자 관리하던 데이터들이 동일한 성격을 지니기 때문에
한개의 컴포넌트(App.vue)로 모아 처리

이 과정에서 @emit -> 부모컴포넌트에서 v-on으로 받기
props를 통해 하위 컴포넌트에 데이터 보내기

많이 일어남


