<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" />
  <OZReportComponent :json-data="jsonData" />
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { ozAppImageUrl } from '@/stores/index';
import { request, btnHandler, showMessage, createOzDataset } from '@/utils/common';
import { initData } from './initWorkDoc';
import dayjs from 'dayjs';

const { t } = useI18n();

const jsonData = ref('');

onMounted(async () => {
  getWorkNm(searchData.value.WORK_DATE);
  await callImagePath();
  jsonData.value = createOzDataset('/WORK/workDoc.ozr', {
    ...ozParam,
    ...{ CSV_DATA: [initData] },
  });
});

const searchData = ref({
  WORK_DATE: dayjs().format('YYYY-MM-DD'),
  WORK_NM: '',
});

const ozParam = {
  IC_CODE: localStorage.getItem('loginIcCode'),
  WORK_DATE: dayjs(searchData.value.WORK_DATE).format('YYYY-MM-DD'),
  WORK_NO: searchData.value.WORK_NM,
  IMAGE_URL: '',
};

const callImagePath = async () => {
  const reportAppCode = await request('post', '/api/common/getImagePath', {
    PRG_CODE: '0201',
  });
  ozParam.IMAGE_URL = ozAppImageUrl + reportAppCode.RPT_APP_CODE;
};

const workNmOption = ref([]);

const searchHeader = ref([
  { label: '근무일자', key: 'WORK_DATE', type: 'date', dateType: 'day' },
  {
    label: '근무번호',
    key: 'WORK_NM',
    type: 'select',
    option: workNmOption,
    width: '213px',
  },
]);

watch(
  () => searchData.value.WORK_DATE,
  (newDate, oldDate) => {
    if (newDate !== oldDate) {
      getWorkNm(newDate);
      searchData.value.WORK_NM = '';
    }
  },
);

const getWorkNm = async (date) => {
  try {
    const response = await request('post', '/api/work/getWorkDocWorkNm', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      WORK_DATE: dayjs(date).format('YYYYMMDD'),
    });
    workNmOption.value = response;
    if (response && Array.isArray(response)) {
      workNmOption.value = response.map((item) => ({
        title: item.WORK_NM,
        value: item.WORK_NO,
      }));
    } else {
      workNmOption.value = [];
    }
    workNmOption.value.unshift({
      title: `종료된 근무: ${workNmOption.value.length}건`,
      value: '',
    });
  } catch (error) {
    alert(t('WORKNO_ERROR'));
  }
};

const handleSearch = async () => {
  if (!searchData.value.WORK_NM) {
    alert(t('SELECT_WORKNO'));
    return;
  }
  try {
    const response = await request('post', '/api/work/getWorkDoc', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      WORK_DATE: dayjs(searchData.value.WORK_DATE).format('YYYYMMDD'),
      WORK_NO: searchData.value.WORK_NM,
    });

    if (response.some((item) => item === null)) {
      showMessage(t('NO_DATA'), 'error');
      jsonData.value = createOzDataset('/WORK/workDoc.ozr', {
        ...ozParam,
        ...{ CSV_DATA: [initData] },
      });
      return;
    } else {
      const mergedData = response.reduce((acc, curr) => {
        if (curr === null) return acc;
        for (const [key, value] of Object.entries(curr)) {
          if (typeof value === 'number') {
            acc[key] = (acc[key] || 0) + value;
          } else {
            acc[key] = value !== null ? value : '';
          }
        }
        return acc;
      }, {});
      mergedData.WORK_DATE = dayjs(mergedData.WORK_DATE, 'YYYYMMDD').format('YYYY-MM-DD');

      jsonData.value = createOzDataset('/WORK/workDoc.ozr', {
        ...ozParam,
        ...{ CSV_DATA: [mergedData] },
      });
    }
  } catch (error) {
    alert('근무확인서' + t('REPORT_ERROR'));
  }
};

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped></style>
