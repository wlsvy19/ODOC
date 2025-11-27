<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <OZReportComponent :json-data="getJsonData" />
</template>

<script setup>
import { ref, computed, onActivated } from 'vue';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { request, btnHandler, createOzDataset, getSystemSmallCode, showMessage } from '@/utils/common';
import { DEFAULT_DAYFINPERIOD_RESULT } from './constants';
import dayjs from 'dayjs';

const getJsonData = computed(() => {
  jsonData.value = createOzDataset('/DAY/dayFinPeriod.ozr', ozParam.value);
  return jsonData.value;
});

onActivated(() => handleSearch());

const searchIcdivOption = getSystemSmallCode('334', true);

const searchHeader = ref([
  { label: '근무기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '영업소구분', key: 'IC_DIV', type: 'select', option: searchIcdivOption, width: '150px' },
]);
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  IC_DIV: '',
});

const jsonData = ref();
const authStore = useAuthStore();

const ozParam = ref({
  CSV_DATA: [{}],
  START_DATE: '',
  END_DATE: '',
  IC_CODE: '',
  IC_DIV: '',
  APPROVAL_IMG_URL: '',
});

const setDefaultEmpty = (data) => {
  if (!data || data.length === 0) {
    showMessage(
      `${dayjs(searchData.value.START_DATE).format('YYYY년 MM월 DD일')} ~ ${dayjs(searchData.value.END_DATE).format(
        'YYYY년 MM월 DD일',
      )} 일마감이 완료되지 않았습니다.`,
      'error',
      5000,
    );
    return DEFAULT_DAYFINPERIOD_RESULT;
  } else return data;
};

const handleSearch = async () => {
  const reportAppCode = await request('post', 'api/common/getImagePath', { PRG_CODE: '0304' });
  if (reportAppCode) ozParam.value.APPROVAL_IMG_URL = ozAppImageUrl + reportAppCode.RPT_APP_CODE;

  const result = await request('post', 'api/day/getDayFinPeriodReport', {
    IC_CODE: authStore.getIcCode,
    START_DATE: searchData.value.START_DATE.replaceAll('-', ''),
    END_DATE: searchData.value.END_DATE.replaceAll('-', ''),
    IC_DIV: searchData.value.IC_DIV,
  });
  ozParam.value.CSV_DATA = setDefaultEmpty(result);
  ozParam.value.START_DATE = searchData.value.START_DATE;
  ozParam.value.END_DATE = searchData.value.END_DATE;
  ozParam.value.IC_CODE = authStore.getIcNm;
  ozParam.value.IC_DIV = searchIcdivOption.find((obj) => obj.value == searchData.value.IC_DIV).title;
};

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped></style>
