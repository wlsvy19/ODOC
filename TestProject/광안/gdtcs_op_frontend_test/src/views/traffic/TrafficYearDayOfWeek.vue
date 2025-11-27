<template>
  <LoadingComponent v-if="isLoading" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    scroll-key="TrafficYearDayOfWeek"
    :headers="mainHeaders"
    :contents="mainContents"
    :height-offset="heightOffset"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
  />
  <OZReportDialog v-model:is-active="isActiveViewer" :json-data="reportRequestData" />
</template>

<script setup>
import { computed, onActivated, ref } from 'vue';
import { request, btnHandler, showMessage, createOzDataset, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { ozAppImageUrl, useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';
import { useRoute } from 'vue-router';

const appTitle = `년단위 요일 교통량`;
const isLoading = ref(false);
const authStore = useAuthStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 */
const heightOffset = 189 + 36 + 29;

onActivated(async () => {
  reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
    PRG_CODE: appCode,
  });
});

const mainHeaders = ref([
  { title: '연월', key: 'MONTH_DP', width: '110' },
  { title: '교통량합계', key: 'SUB_TOT_CNT', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '일요일', key: 'DAY0_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '월요일', key: 'DAY1_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '화요일', key: 'DAY2_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '수요일', key: 'DAY3_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '목요일', key: 'DAY4_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '금요일', key: 'DAY5_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '토요일', key: 'DAY6_CNT', width: '100', customBodyCellStyle: 'table-body-style-right' },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.MONTH_DP === '합계') {
    return 'table-body-style-summary-2';
  } else if (item.MONTH_DP === '일평균') {
    return 'table-body-style-summary-1';
  }
  return '';
};

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'year' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'year' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY'),

  END_DATE: dayjs().format('YYYY'),
});

const prevSearchData = ref({});

const mainContents = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/traffic/getTrafficYearDayOfWeek', {
      START_YEAR: searchData.value['START_DATE'],
      END_YEAR: searchData.value['END_DATE'],
      IC_CODE: authStore.getIcCode,
    });
    prevSearchData.value = { ...searchData.value };
    mainContents.value = response;
    if (mainContents.value.length === 0) {
      showMessage('조회된 데이터가 없습니다.', 'success');
    }
  } catch (error) {
    showMessage('데이터 조회 중 오류 발생:', 'error');
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = async () => {
  const headerRow = 1;
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    excelDownload(headerRow, searchHeader.value, prevSearchData.value, mainHeaders.value, mainContents.value, appTitle, appTitle);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

//#region Report
const isActiveViewer = ref(false);
const reportRequestData = ref('');

const handlePrint = () => {
  if (mainContents.value.length === 0) {
    showMessage(`조회된 데이터가 없습니다.`, 'warning');
  } else {
    reportRequestData.value = createOzDataset('/TRAFFIC/trafficYearDayOfWeek.ozr', {
      CSV_DATA: mainContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: appTitle,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, prevSearchData.value),
    });
    isActiveViewer.value = true;
  }
};
//#endregion

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
  Print: handlePrint,
});
</script>
