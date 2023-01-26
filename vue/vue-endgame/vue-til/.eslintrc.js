module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: ["plugin:vue/essential", "@vue/prettier"],
  rules: {
    // 에러 나타나도 콘솔에 출력 하지말고 화면 나오게 해
    "no-console": "off",

    // "no-console": process.env.NODE_ENV === "production" ? "error" : "off",
    // "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off"
    
    // 프리티어: 여러 사람이 동일한 패턴으로 개발
    // esLint와 충돌 나기 때문에 프리티어를 여기서 정의 해줘야 함 -> 여기서 린트와 프리티어 같이 사용
    "prettier/prettier": ['error', { // 포맷 안맞으면 에러 뿜음 -> 저장시 자동 고침
      singleQuote: true,
      semi: true,
      useTabs: false,
      tabWidth: 2,
      trailingComma: 'all',
      printWidth: 80,
      bracketSpacing: true,
      arrowParens: 'avoid',
    }]
  },
  parserOptions: {
    parser: "babel-eslint"
  },
  overrides: [
    {
      files: [
        "**/__tests__/*.{j,t}s?(x)",
        "**/tests/unit/**/*.spec.{j,t}s?(x)"
      ],
      env: {
        jest: true
      }
    }
  ]
};
