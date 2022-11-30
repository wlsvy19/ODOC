const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false, // ESLint 맞춤법 검사 끄기
  devServer: {
    client: {
      overlay: false,
    },
  },
});
