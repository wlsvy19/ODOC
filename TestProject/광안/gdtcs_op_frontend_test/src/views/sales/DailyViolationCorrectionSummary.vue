<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current" />
  </div>
  <TableComponent
    scroll-key="DailyViolationCorrectionSummary"
    :headers="mainHeaders"
    :contents="mainContents"
    :height-offset="heightOffset"
    row-type="mix"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
  />
</template>

<script setup>
import { reactive, ref } from 'vue';
import { request, btnHandler, showMessage } from '@/utils/common';
import dayjs from 'dayjs';
import { useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';

const appTitle = `일별사무실유형현황`;
const isLoading = ref(false);
const authStore = useAuthStore();

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 */
const heightOffset = 189 + 36 + 29 * 2;

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

const mainHeaders = ref([
  {
    title: '구분',
    children: [
      { title: '근무일자', key: 'WORK_DATE_DP', width: '100' },
      { title: '처리유형', key: 'HAND_TYPE_DP', width: '90' },
      { title: '위반코드', key: 'VLTN_CODE_DP', width: '160' },
      { title: '지불수단', key: 'VLTN_PAY_TYPE_DP', width: '100' },
      { title: '요금구분', key: 'OFC_DIV_DP', width: '100' },
    ],
  },
  {
    title: '수납할 금액',
    children: [
      { title: '처리건수', key: 'OFC_CNT_DP', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '통행료', key: 'OFC_TO_PAY_DP', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납한 금액',
    children: [
      { title: '처리건수', key: 'OFC_PAY_CNT_DP', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '통행료', key: 'OFC_PAY_FARE_DP', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '미납 금액',
    children: [
      { title: '처리건수', key: 'OFC_UNP_CNT_DP', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '통행료', key: 'OFC_UNP_FARE_DP', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '정상 금액',
    children: [
      { title: '처리건수', key: 'OFC_NOR_CNT_DP', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '통행료', key: 'OFC_NOR_FARE_DP', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.VLTN_CODE_DP === '소계') {
    return 'table-body-style-summary-2';
  }
  if (item.VLTN_CODE_DP === '총합계') {
    return 'table-body-style-summary-1';
  }
  return '';
};

const mainContents = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/sales/getDailyViolationCorrectionSummary', {
      START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
      END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
      IC_CODE: authStore.getIcCode,
    });
    mainContents.value = response;
    searchData.prev = { ...searchData.current };
    if (mainContents.value.length === 0) {
      showMessage('데이터가 없습니다.');
    }
  } catch (e) {
    showMessage(`데이터 조회 중 오류가 발생했습니다.`, 'error');
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = async () => {
  const headerRow = 2;
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
