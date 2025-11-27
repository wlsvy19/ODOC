<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current" />
  </div>
  <TableComponent
    scroll-key="DailyReductionSummaryBs"
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

const appTitle = `일별 감면현황-부산시`;
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
      { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
      { title: '소계', key: 'ALL_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '부산면제',
    children: [
      { title: '면제아님', key: 'C01_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '조회불능', key: 'C02_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '다자녀', key: 'C03_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '유공자', key: 'C04_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '장애인', key: 'C05_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '전기차', key: 'C06_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '수소차', key: 'C07_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '택시', key: 'C08_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '우수기업인', key: 'C09_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '우수납세자', key: 'C10_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '효행자', key: 'C11_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '모범노동자', key: 'C12_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '장애인복지', key: 'C13_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '두리발', key: 'C14_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '시내순환', key: 'C15_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '시내/공항버스', key: 'C16_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '저공해', key: 'C17_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '유지보수', key: 'C18_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.WORK_DATE_DP === '합계') {
    return 'table-body-style-summary-1';
  }
  return '';
};

const mainContents = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/sales/getDailyExemptSummaryBs', {
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
