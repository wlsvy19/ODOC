<template>
  <LoadingComponent v-if="isLoading" />
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    scroll-key="TrafficPreMonth"
    :headers="mainHeaders"
    :contents="mainContents"
    :height-offset="heightOffset"
    rowType="mix"
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

const appTitle = `전월 대비 교통량`;
const isLoading = ref(false);
const authStore = useAuthStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 */
const heightOffset = 189 + 36 + 29 * 2;

onActivated(async () => {
  reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
    PRG_CODE: appCode,
  });
});

const mainHeaders = ref([
  {
    title: '구분',
    align: 'center',
    children: [
      { title: '월', key: 'WORK_MONTH_DP', align: 'center', width: '100' },
      { title: '방향', key: 'DIR_DIV_DP', align: 'center', width: '120' },
    ],
  },
  {
    title: '차종구분',
    align: 'center',
    children: [
      { title: '경형', key: 'CAR1_CNT_DP', align: 'center ', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소형', key: 'CAR2_CNT_DP', align: 'center', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '중형', key: 'CAR3_CNT_DP', align: 'center', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '대형', key: 'CAR4_CNT_DP', align: 'center', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '합계', key: 'CAR_TOT_CNT_DP', align: 'center', width: '120', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '징수구분',
    align: 'center',
    children: [
      { title: '하이패스선불', key: 'PAY1_CNT_DP', align: 'center', width: '120', customBodyCellStyle: 'table-body-style-right' },
      { title: '하이패스후불', key: 'PAY2_CNT_DP', align: 'center', width: '120', customBodyCellStyle: 'table-body-style-right' },
      { title: '면제', key: 'PAY3_CNT_DP', align: 'center', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '위반', key: 'PAY4_CNT_DP', align: 'center', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '사전등록', key: 'PAY5_CNT_DP', align: 'center', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.DIR_DIV_DP === '계') {
    return 'table-body-style-summary-1';
  }
  return '';
};

const searchHeader = ref([{ label: '년월', key: 'MONTH', type: 'date', dateType: 'month' }]);

const searchData = ref({
  MONTH: dayjs().format('YYYY-MM'),
});

const prevSearchData = ref({});

const mainContents = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/traffic/getTrafficPreMonth', {
      START_MONTH: dayjs(searchData.value['MONTH']).subtract(1, 'month').format('YYYYMM'),
      END_MONTH: searchData.value['MONTH'].replaceAll('-', ''),
      IC_CODE: authStore.getIcCode,
    });
    prevSearchData.value = { ...searchData.value };
    mainContents.value = response;
    if (mainContents.value.length === 0) {
      showMessage(`조회된 데이터가 없습니다.`, 'success');
      return;
    }
  } catch (error) {
    showMessage('데이터 조회 중 오류 발생:', 'error');
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
    reportRequestData.value = createOzDataset('/TRAFFIC/trafficPreMonth.ozr', {
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

<style scoped />
