import { defineAsyncComponent } from 'vue';

import AppLayout from '@/components/layouts/AppLayout.vue';

import ButtonComponent from '@/components/common/ButtonComponent.vue';
import DateComponent from '@/components/common/DateComponent.vue';
import DatefieldComponent from '@/components/common/DatefieldComponent.vue';
import DatepickerComponent from '@/components/common/DatepickerComponent.vue';
import TimepickerComponent from '@/components/common/TimepickerComponent.vue';
import DatetimepickerComponent from '@/components/common/DatetimepickerComponent.vue';
import LoadingComponent from '@/components/common/LoadingComponent.vue';
import OZReportComponent from '@/components/common/OZReportComponent.vue';
import SearchDataComponent from '@/components/common/SearchDataComponent.vue';
import SnackbarComponent from '@/components/common/SnackbarComponent.vue';
import InformationComponent from '@/components/common/InformationComponent.vue';

import DialogInputFormGridComponent from '@/components/dialog/DialogInputFormGridComponent.vue';
import InputFormGrid from '@/components/common/InputFormGrid.vue';
import DialogButtonNavi from '@/components/dialog/DialogButtonNavi.vue';
import OZReportDialog from '@/components/dialog/OZReportDialog.vue';
import DialogModifyPassword from '@/components/dialog/DialogModifyPassword.vue';

import GridSystemComponent from '@/components/grid/GridSystemComponent.vue';
import TableComponent from '@/components/grid/TableComponent.vue';

import SiteMap from '@/components/dialog/SiteMap.vue';

export default {
  install: (app) => {
    app.component('AppLayout', AppLayout);
    app.component(
      'MainDashboard',
      defineAsyncComponent(() => import('@/views/MainDashboard.vue')),
    );
    app.component(
      'AuthLogin',
      defineAsyncComponent(() => import('@/components/authentication/AuthLogin.vue')),
    );

    app.component('ButtonComponent', ButtonComponent);
    app.component('DateComponent', DateComponent);
    app.component('DatefieldComponent', DatefieldComponent);
    app.component('DatepickerComponent', DatepickerComponent);
    app.component('TimepickerComponent', TimepickerComponent);
    app.component('DatetimepickerComponent', DatetimepickerComponent);
    app.component('LoadingComponent', LoadingComponent);
    app.component('OZReportComponent', OZReportComponent);
    app.component('SearchDataComponent', SearchDataComponent);
    app.component('SnackbarComponent', SnackbarComponent);
    app.component('InformationComponent', InformationComponent);

    app.component('DialogButtonNavi', DialogButtonNavi);
    app.component('DialogInputFormGridComponent', DialogInputFormGridComponent);
    app.component('InputFormGrid', InputFormGrid);
    app.component('OZReportDialog', OZReportDialog);
    app.component('DialogModifyPassword', DialogModifyPassword);

    app.component('GridSystemComponent', GridSystemComponent);
    app.component('TableComponent', TableComponent);

    app.component('SiteMap', SiteMap);
  },
};
