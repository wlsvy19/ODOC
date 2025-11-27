<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current" />
  </div>
  <TableComponent
    scroll-key="DiscountSummary"
    :headers="mainHeaders"
    :contents="mainContents"
    :height-offset="heightOffset"
    :height-percent="mainGridHeightPercent"
  />
  <v-row class="bottom-contents-title"><span>소계</span></v-row>
  <TableComponent
    scroll-key="DiscountSummaryFooter"
    :headers="summaryHeaders"
    :contents="summaryContents"
    :height-offset="heightOffsetFooter"
    :height-percent="100 - mainGridHeightPercent"
    row-type="1"
  />
  <OZReportDialog v-model:is-active="isActiveViewer" :json-data="reportRequestData" />
</template>

<script setup>
import { computed, nextTick, onActivated, reactive, ref } from 'vue';
import { request, btnHandler, showMessage, createOzDataset, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { ozAppImageUrl, useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';
import { useRoute } from 'vue-router';

//#region Common
const appTitle = `할인내역보고서`;
const isLoading = ref(false);
const authStore = useAuthStore();
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');
const reportConfirmInfo = ref({});

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 */
const heightOffset = 189 + 36 + 29 * 2 + 26;
const heightOffsetFooter = heightOffset;
const mainGridHeightPercent = 81.2;

onActivated(() => {
  nextTick(async () => {
    reportConfirmInfo.value = await request('post', 'api/common/getImagePath', {
      PRG_CODE: appCode,
    });
  });
});
//#endregion

//#region Search
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
//#endregion

//#region Main Grid
const mainHeaders = ref([
  { title: '할인구분', key: 'DC_DIV_DP', width: '130' },
  { title: '구분', key: 'VAL_DIV_DP', width: '90' },
  { title: '경형', key: 'TYPE1_VAL', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '소형', key: 'TYPE2_VAL', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '중형', key: 'TYPE3_VAL', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '대형', key: 'TYPE4_VAL', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '합계', key: 'SMR_VAL', width: '130', customBodyCellStyle: 'table-body-style-right' },
]);

const mainContents = ref([]);
//#endregion

const summaryHeaders = ref([
  { title: '차종', key: 'CAR_TYPE', width: '90' },
  { title: '대수', key: 'CAR_CNT', width: '90', customBodyCellStyle: 'table-body-style-right' },
  { title: '원통행료', key: 'ORIGIN_FARE', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '할인요금', key: 'DC_FARE', width: '120', customBodyCellStyle: 'table-body-style-right' },
  { title: '수납요금', key: 'PAY_FARE', width: '120', customBodyCellStyle: 'table-body-style-right' },
]);

const summaryContents = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/sales/getDiscountSummary', {
      START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
      END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
      IC_CODE: authStore.getIcCode,
    });
    searchData.prev = { ...searchData.current };
    mainContents.value = response.discountList;
    summaryContents.value = response.discountSummary;
    if (mainContents.value.length === 0) {
      showMessage(`조회된 데이터가 없습니다.`, 'success');
      return;
    }
  } catch (error) {
    showMessage(`${error}`, 'error');
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
    excelDownload(headerRow, searchHeader.value, searchData.prev, mainHeaders.value, mainContents.value, appTitle, appTitle);
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
    reportRequestData.value = createOzDataset('/SALES/discountSummary.ozr', {
      CSV_DATA: mainContents.value,
      CSV_DATA2: summaryContents.value,
      IC_NAME: authStore.getIcNm,
      REPORT_NM: appTitle,
      APPROVAL_IMG_URL: ozAppImageUrl + reportConfirmInfo.value.RPT_APP_CODE,
      ...getCondition(searchHeader.value, searchData.prev),
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

<style scoped>
.bottom-contents-title {
  background-color: #f5f5f5;
  border-top: 1px solid #a9a9a9;
  padding: 2px 0px;
}
.bottom-contents-title > span {
  margin-left: 10px;
  color: #666666;
  font-size: 14px;
  font-weight: 600;
}
</style>
