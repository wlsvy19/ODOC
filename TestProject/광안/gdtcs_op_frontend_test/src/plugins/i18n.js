// src/plugins/i18n.js
import { createI18n } from 'vue-i18n';
import ko from '@/locales/ko.json';
import en from '@/locales/en.json';

const i18n = createI18n({
  legacy: false, // Composition API 사용을 위해 legacy 모드 비활성화
  locale: 'ko', // 기본 언어
  fallbackLocale: 'en',
  messages: {
    ko,
    en,
  },
});

export default i18n;
