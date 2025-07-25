<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent :heightOffset="282" scrollKey="dailyViolationData" rowType="mix" :headers="headers" :contents="dailyViolationData" />
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, computed } from 'vue';
import { request, btnHandler, createOzDataset } from '@/utils/common';
import dayjs from 'dayjs';
import { excelDownload } from '@/utils/excel';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const authStore = useAuthStore();
const isLoading = ref(false);
const isActive = ref(false);
const jsonData = ref('');

const headers = ref([
  { title: '영업소명', key: 'PLZ_NM', width: '150' },
  { title: '위반코드', key: 'VLTN_CODE_NAME', width: '150' },
  { title: '구분', key: 'DIV', width: '100' },
  {
    title: '21',
    children: [
      { title: 'IR', key: 'LANE21_IR', width: '100' },
      { title: 'RF', key: 'LANE21_RF', width: '100' },
      { title: '소계', key: 'LANE21', width: '100' },
    ],
  },
  {
    title: '22',
    children: [
      { title: 'IR', key: 'LANE22_IR', width: '100' },
      { title: 'RF', key: 'LANE22_RF', width: '100' },
      { title: '소계', key: 'LANE22', width: '100' },
    ],
  },
  {
    title: '25',
    children: [
      { title: 'IR', key: 'LANE25_IR', width: '100' },
      { title: 'RF', key: 'LANE25_RF', width: '100' },
      { title: '소계', key: 'LANE25', width: '100' },
    ],
  },
  {
    title: '26',
    children: [
      { title: 'IR', key: 'LANE26_IR', width: '100' },
      { title: 'RF', key: 'LANE26_RF', width: '100' },
      { title: '소계', key: 'LANE26', width: '100' },
    ],
  },
  {
    title: '81',
    children: [
      { title: 'IR', key: 'LANE81_IR', width: '100' },
      { title: 'RF', key: 'LANE81_RF', width: '100' },
      { title: '소계', key: 'LANE81', width: '100' },
    ],
  },
  {
    title: '82',
    children: [
      { title: 'IR', key: 'LANE82_RF', width: '100' },
      { title: 'RF', key: 'LANE82_IR', width: '100' },
      { title: '소계', key: 'LANE82', width: '100' },
    ],
  },
  {
    title: '85',
    children: [
      { title: 'IR', key: 'LANE85_IR', width: '100' },
      { title: 'RF', key: 'LANE85_RF', width: '100' },
      { title: '소계', key: 'LANE85', width: '100' },
    ],
  },
  {
    title: '86',
    children: [
      { title: 'IR', key: 'LANE86_IR', width: '100' },
      { title: 'RF', key: 'LANE86_RF', width: '100' },
      { title: '소계', key: 'LANE86', width: '100' },
    ],
  },
  {
    title: '95',
    children: [
      { title: 'IR', key: 'LANE95_IR', width: '100' },
      { title: 'RF', key: 'LANE95_RF', width: '100' },
      { title: '소계', key: 'LANE95', width: '100' },
    ],
  },
  {
    title: '96',
    children: [
      { title: 'IR', key: 'LANE96_IR', width: '100' },
      { title: 'RF', key: 'LANE96_RF', width: '100' },
      { title: '소계', key: 'LANE96', width: '100' },
    ],
  },

  { title: '총합계', key: 'TOTAL', width: '100' },
  { title: '위반비율', key: 'VLTN_RATE', width: '100' },
]);

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),

  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const dailyViolationData = ref([]);

const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/sales/getDailyViolation', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    if (data.length == 0) {
      alert(`데이터가 없습니다`);
    }
    dailyViolationData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));
  } catch (error) {
    alert(`일별 위반현황 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 'mix';
  if (dailyViolationData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, dailyViolationData.value, '일별 위반현황', '일별 위반현황');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (dailyViolationData.value.length === 0) {
      alert(`조회된 데이터가 없습니다.`);
      return;
    }
    if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
      alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
      return;
    }
    const imagePath = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
    jsonData.value = createOzDataset('/SALES/dailyViolation.ozr', {
      CSV_DATA: dailyViolationData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      TITLE_NM: '일별 위반현황',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
    });
    isActive.value = true;
  } catch (error) {
    alert(`일별 위반현황 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};
btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>

<style scoped></style>
