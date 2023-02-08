module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
  },
  parserOptions: {
    parser: 'babel-eslint',
  },
  extends: [
    '@nuxtjs',
    'plugin:prettier/recommended',
    'plugin:nuxt/recommended',
  ],
  plugins: [],
  // add your custom rules here
  rules: {
    // 에러 나도 콘솔에 띄워
    'no-console': 'off',
    'prettier/prettier': [
      'error',
      {
        // Delete 어쩌고 뜨는 에러 잡기
        endOfLine: 'auto',
      },
    ],
  },
}
