export default {
  // Target: https://go.nuxtjs.dev/config-target
  // ! Static Site Generator 방식: netlify 사용
  target: 'static',

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    title: 'nuxt',
    htmlAttrs: {
      lang: 'en',
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  // * 전역 css 설정
  css: ['@/assets/css/reset.css'],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/eslint
    '@nuxtjs/eslint-module',
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [],

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {},

  // * 서버포트 변경: https://nuxtjs.org/docs/features/configuration/#edit-host-and-port
  // process.env.NODE_ENV: nuxt에서 제공하는 환경변수
  // 개발일땐 4000포트, 배포일땐 null
  // 개발: development
  server: {
    port: process.env.NODE_ENV === 'production' ? null : 4000,
  },

  // * env: api폴더 에 있는 axios 관련 URL 설정
  // 배포할땐 https 로 배포
  // 개발할 땐 로컬 3000번 포트
  env: {
    baseURL:
      process.env.NODE_ENV === 'production'
        ? 'https://my-json-server.typicode.com/joshua1988/nuxt-shopping-api'
        : 'http://localhost:3000',
  },
}
