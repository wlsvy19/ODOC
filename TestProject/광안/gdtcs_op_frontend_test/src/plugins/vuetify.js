import { createVuetify } from 'vuetify';
import { mdi } from 'vuetify/iconsets/mdi';
import { en, ko } from 'vuetify/locale';

import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import * as labsComponents from 'vuetify/labs/components';

import 'vuetify/styles';
import '@mdi/font/css/materialdesignicons.min.css';
import '@mdi/font/css/materialdesignicons.css';

const vuetify = createVuetify({
  components: {
    ...components,
    ...labsComponents,
  },
  directives,
  locale: {
    messages: { en, ko },
  },
  icons: {
    defaultSet: 'mdi',
    aliases: {
      cancel: 'mdi-cancel',
      menu: 'mdi-menu',
      sets: {
        mdi,
      },
    },
  },
});

export default vuetify;
