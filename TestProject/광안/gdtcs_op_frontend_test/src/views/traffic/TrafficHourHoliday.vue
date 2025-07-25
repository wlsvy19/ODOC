<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    scroll-key="TrafficHourHoliday"
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

const appTitle = `시간별 공휴일 교통량 집계`;
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

const searchHeader = ref([
  { label: '일자', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
]);

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const prevSearchData = ref({});

const mainHeaders = ref([
  {
    title: '구분',
    align: 'center',
    children: [{ title: '시간', key: 'HR_DP', width: '90' }],
  },
  {
    title: '평일',
    align: 'center',
    children: [
      { title: '합계', key: 'DIV1_CNT', width: '120', customBodyCellStyle: 'table-body-style-right' },
      { title: '평균', key: 'DIV1_AVG', width: '90', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '공휴일',
    align: 'center',
    children: [
      { title: '합계', key: 'DIV2_CNT', width: '120', customBodyCellStyle: 'table-body-style-right' },
      { title: '평균', key: 'DIV2_AVG', width: '90', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '전체',
    align: 'center',
    children: [
      { title: '합계', key: 'SUB_TOT_CNT', width: '120', customBodyCellStyle: 'table-body-style-right' },
      { title: '평균', key: 'SUB_TOT_AVG', width: '90', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.HR_DP === '합계') {
    return 'table-body-style-summary-1';
  }
  return '';
};


const mainContents = ref([]);

const handleSearch = async () => {
  try {
    const response = await request('post', 'api/traffic/getTrafficHourHoliday', {
      START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
      END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
      IC_CODE: authStore.getIcCode,
    });
    prevSearchData.value = { ...searchData.value };
    mainContents.value = response;
    if (mainContents.value.length === 0) {
      showMessage('조회된 데이터가 없습니다.', 'success');
    }
  } catch (error) {
    showMessage('데이터 조회 중 오류가 발생했습니다.', 'error');
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
    showMessage('조회된 데이터가 없습니다.', 'warning');
  } else {
    reportRequestData.value = createOzDataset('/TRAFFIC/trafficHourHoliday.ozr', {
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
