<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :heightOffset="285"
    rowType="mix"
    scrollKey="trafficDailyPayDivData"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
    :headers="headers"
    :contents="trafficDailyPayDivData"
  />
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

const isActive = ref(false);
const jsonData = ref('');
const isLoading = ref(false);

const headers = ref([
  { title: '근무일자', key: 'TRF_DATE', width: '100' },
  {
    title: '차종구분',
    children: [
      { title: '경형', key: 'LIGHT_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소형', key: 'SMALLSIZE_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '중형', key: 'FULLSIZED_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '대형', key: 'SPECIAL_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
      { title: '소계', key: 'CARTYPE_TOTAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  {
    title: '수납구분(일반)',
    children: [
      { title: '전자카드(선불)', key: 'EP_CARD', width: '150', customBodyCellStyle: 'table-body-style-right' },
      { title: '전자카드(후불)', key: 'EL_CARD', width: '150', customBodyCellStyle: 'table-body-style-right' },
    ],
  },
  { title: '면제', key: 'EXMT', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '위반', key: 'VI', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '사전등록', key: 'PRE_REG', width: '100', customBodyCellStyle: 'table-body-style-right' },
]);

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
]);
const setCustomBodyRowStyleSummary = (item) => {
  if (item.TRF_DATE === '합계') {
    return 'table-body-style-summary-1';
  }
  return '';
};
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
});

const trafficDailyPayDivData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/traffic/getTrafficDailyPayDiv', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    trafficDailyPayDivData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
  } catch (error) {
    alert(`일별/수납구분별 교통량 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 'mix';
  if (trafficDailyPayDivData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;
    excelDownload(
      row,
      searchHeader.value,
      searchData.value,
      excelHeaders,
      trafficDailyPayDivData.value,
      '일별수납구분별 교통량 조회',
      '일별수납구분별 교통량 조회',
    );
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (trafficDailyPayDivData.value.length === 0) {
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
    jsonData.value = createOzDataset('/TRAFFIC/trafficDailyPayDiv.ozr', {
      CSV_DATA: trafficDailyPayDivData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      TITLE_NM: '일별_수납구분별 교통량 조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
    });
    isActive.value = true;
  } catch (error) {
    alert(`일별/수납구분별 교통량 조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
