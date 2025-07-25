<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <OZReportComponent :json-data="getJsonData" />
</template>

<script setup>
import { ref, computed, onActivated } from 'vue';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { request, btnHandler, createOzDataset, getSystemSmallCode, showMessage } from '@/utils/common';
import { DEFAULT_DAYFIN_RESULT } from './constants';
import dayjs from 'dayjs';

const getJsonData = computed(() => {
  jsonData.value = createOzDataset('/DAY/dayFinNew.ozr', ozParam.value);
  return jsonData.value;
});

onActivated(() => handleSearch());

const searchIcdivOption = getSystemSmallCode('334', true);

const searchHeader = ref([
  { label: '근무일자', key: 'WORK_DATE', type: 'date', dateType: 'day' },
  { label: '영업소구분', key: 'IC_DIV', type: 'select', option: searchIcdivOption, width: '150px' },
]);
const searchData = ref({
  WORK_DATE: dayjs().format('YYYY-MM-DD'),
  IC_DIV: '',
});

const jsonData = ref();
const authStore = useAuthStore();

const ozParam = ref({
  CSV_DATA: DEFAULT_DAYFIN_RESULT,
  CSV_DATA_MONTH: DEFAULT_DAYFIN_RESULT,
  CSV_DATA_YEAR: DEFAULT_DAYFIN_RESULT,
  WORK_DATE: '',
  IC_CODE: '',
  IC_DIV: '',
  APPROVAL_IMG_URL: '',
});

const isEmpty = (data) => {
  if (!data || data.length === 0) return true;
  else return false;
};

const handleSearch = async () => {
  const reportAppCode = await request('post', 'api/common/getImagePath', { PRG_CODE: '0304' });
  if (reportAppCode) ozParam.value.APPROVAL_IMG_URL = ozAppImageUrl + reportAppCode.RPT_APP_CODE;

  const result = await request('post', 'api/day/getDayFinReportNew', {
    IC_CODE: authStore.getIcCode,
    WORK_DATE: searchData.value.WORK_DATE.replaceAll('-', ''),
    IC_DIV: searchData.value.IC_DIV,
  });

  if (isEmpty(result?.day)) {
    showMessage(`${dayjs(searchData.value.WORK_DATE).format('YYYY년 MM월 DD일')} 사전등록/공차택시 반영 재집계 처리가 완료되지 않았습니다.`, 'error', 5000);
    ozParam.value.CSV_DATA = DEFAULT_DAYFIN_RESULT;
    ozParam.value.CSV_DATA_MONTH = DEFAULT_DAYFIN_RESULT;
    ozParam.value.CSV_DATA_YEAR = DEFAULT_DAYFIN_RESULT;
  } else {
    ozParam.value.CSV_DATA = result.day;
    ozParam.value.CSV_DATA_MONTH = result.month;
    ozParam.value.CSV_DATA_YEAR = result.year;
  }
  ozParam.value.WORK_DATE = searchData.value.WORK_DATE;
  ozParam.value.IC_CODE = authStore.getIcNm;
  ozParam.value.IC_DIV = searchIcdivOption.find((obj) => obj.value == searchData.value.IC_DIV).title;
};

btnHandler({
  Search: handleSearch,
});
</script>

<style scoped></style>
