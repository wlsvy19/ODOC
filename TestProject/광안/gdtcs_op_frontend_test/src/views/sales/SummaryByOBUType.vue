<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current" />
  </div>
  <TableComponent scroll-key="SummaryByOBUType" :headers="mainHeaders" :contents="mainContents" :height-offset="heightOffset" row-type="mix" />
</template>

<script setup>
import { reactive, ref } from 'vue';
import { request, btnHandler, showMessage, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';

const appTitle = `OBU 타입별 처리내역`;
const isLoading = ref(false);
const authStore = useAuthStore();

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 */
const heightOffset = 189 + 36 + 29 * 2;

const mainHeaders = ref([
  { title: '근무일자', key: 'WORK_DATE_DP', align: 'center', width: '110' },
  {
    title: '차로',
    align: 'center',
    children: [
      { title: '번호', key: 'LANE_NO', align: 'center', width: '70' },
      { title: '차로명', key: 'LANE_NAME', width: '90' },
    ],
  },
  {
    title: '위반코드',
    align: 'center',
    children: [
      { title: '코드', key: 'VLTN_CODE', align: 'center', width: '70' },
      { title: '코드명', key: 'VLTN_CODE_DP', width: '160' },
    ],
  },
  {
    title: 'IR OBU',
    align: 'center',
    children: [
      { title: '대수', key: 'IR_CNT', align: 'end', width: '100' },
      { title: '비율(%)', key: 'IR_RATE', align: 'end', width: '90' },
    ],
  },
  {
    title: 'RF OBU',
    align: 'center',
    children: [
      { title: '대수', key: 'RF_CNT', align: 'end', width: '100' },
      { title: '비율(%)', key: 'RF_RATE', align: 'end', width: '90' },
    ],
  },
  {
    title: '기타',
    align: 'center',
    children: [
      { title: '대수', key: 'ETC_CNT', align: 'end', width: '100' },
      { title: '비율(%)', key: 'ETC_RATE', align: 'end', width: '90' },
    ],
  },
  { title: '소계', key: 'SUB_CNT', align: 'end', width: '120' },
]);

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
]);

const searchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
  },
  prev: {},
});

const mainContents = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/sales/getSummaryByOBUType', {
      START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
      END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
      IC_CODE: authStore.getIcCode,
    });
    searchData.prev = getCondition(searchHeader.value, searchData.current);
    mainContents.value = response;
    if (response.length === 0) {
      showMessage('조회된 데이터가 없습니다.', 'info');
    }
  } catch (error) {
    showMessage(`${error}`, 'error');
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = async () => {
  const headerRow = 'mix';
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeader.value, searchData.prev, mainHeaders.value, mainContents.value, appTitle, appTitle);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>

<style scoped />
