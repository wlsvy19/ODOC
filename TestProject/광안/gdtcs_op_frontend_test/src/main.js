import '@/assets/css/main.css';
import '@/assets/css/font.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from '@/App.vue';
import router from '@/router';
import vuetify from '@/plugins/vuetify';
import component from '@/plugins/component';

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import setupCalendar from 'v-calendar';
import 'v-calendar/style.css';

import i18n from '@/plugins/i18n';

// 개발 환경에서 <Suspense> 경고 메시지 안나오도록 설정
if (import.meta.env.MODE === 'development') {
  const originalInfo = console.info;

  console.info = function (message, ...optionalParams) {
    if (message.includes('<Suspense> is an experimental feature and its API will likely change.')) {
      return;
    }
    originalInfo.call(console, message, ...optionalParams);
  };
}

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.use(vuetify);
app.use(component);

app.use(ElementPlus);
app.use(setupCalendar, { componentPrefix: 'nathan' });

app.use(i18n);

app.mount('#app');
