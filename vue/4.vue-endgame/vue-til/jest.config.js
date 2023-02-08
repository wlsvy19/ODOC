module.exports = {
  preset: '@vue/cli-plugin-unit-jest',

  //원래는 test/unit에 작성 해야 함
  // testMatch: 테스트 파일이 테스트 하려는 파일과 근접한 경로에 만들도록 지원
  testMatch: [
    '<rootDir>/src/**/*.spec.(js|jsx|ts|tsx)|**/__tests__/*.(js|jsx|ts|tsx)',
  ],
};
