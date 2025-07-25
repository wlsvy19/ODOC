<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <OZReportComponent :json-data="jsonData" />
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { request, btnHandler, createOzDataset, showMessage } from '@/utils/common';
import { ozAppImageUrl } from '@/stores/index';
import dayjs from 'dayjs';

const { t } = useI18n();

const jsonData = ref('');

onMounted(async () => {
  await handleSearch();
});

const searchHeader = ref([{ label: '근무 월', key: 'WORK_DATE', type: 'date', dateType: 'month' }]);

const searchData = ref({
  WORK_DATE: dayjs().format('YYYY-MM'),
  IC_DIV: '',
});

const ozParam = {
  IC_CODE: localStorage.getItem('loginIcCode'),
  WORK_DATE: dayjs(searchData.value.WORK_DATE).format('YYYY-MM'),
};

const handleSearch = async () => {
  try {
    const imagePath = await request('post', '/api/common/getImagePath', {
      PRG_CODE: '0103',
    });
    const response = await request('post', '/api/sales/getMonthTollFeeReport', {
      IC_CODE: localStorage.getItem('loginIcCode'),
      WORK_DATE: dayjs(searchData.value.WORK_DATE).format('YYYYMM'),
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
    });
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

    ozParam.WORK_DATE = dayjs(searchData.value.WORK_DATE).format('YYYY년 MM월');

    jsonData.value = createOzDataset('/SALES/monthTollFee.ozr', {
      ...ozParam,
      ...{ CSV_DATA: [mergedData] },
    });
  } catch (error) {
    alert('월간통행료징수현황(월보)' + t('REPORT_ERROR'));
  }
};

let timer = null;
watch(
  () => searchData.value.WORK_DATE,
  async () => {
    if (timer) clearTimeout(timer);
    timer = setTimeout(async () => {
      await handleSearch();
    }, 300); // ms
  },
);

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped></style>
