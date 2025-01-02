require('@rushstack/eslint-patch/modern-module-resolution');

module.exports = {
  root: true,
  extends: [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/eslint-config-prettier',
  ],
  env: {
    'vue/setup-compiler-macros': true,
    node: true, // Node.js 환경을 위한 설정 추가
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off', // 개발 환경에서는 콘솔 로그 사용 허용
    'prettier/prettier': [
      'error',
      {
        singleQuote: true,
        useTabs: false,
        tabWidth: 2,
        trailingComma: 'all',
        printWidth: 80,
        bracketSpacing: true,
        arrowParens: 'avoid',
      },
    ],
  },
};
