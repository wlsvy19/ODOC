<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :heightOffset="265"
    rowType="1"
    :headers="headers"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
    :contents="trafficLineHourData"
    scrollKey="trafficLineHourData"
  />
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />

  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, computed, watch } from 'vue';
import { request, btnHandler, createOzDataset, getCondition } from '@/utils/common';
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
  { title: '순번', key: 'ROW_NUMBER', width: '100' },
  { title: '일자', key: 'TRF_DATE', width: '100' },
  { title: '차로명', key: 'LANE_NM', width: '150' },
  { title: '합계', key: 'TRF_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '00시', key: 'TIME_00', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '01시', key: 'TIME_01', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '02시', key: 'TIME_02', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '03시', key: 'TIME_03', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '04시', key: 'TIME_04', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '05시', key: 'TIME_05', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '06시', key: 'TIME_06', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '07시', key: 'TIME_07', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '08시', key: 'TIME_08', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '09시', key: 'TIME_09', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '10시', key: 'TIME_10', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '11시', key: 'TIME_11', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '12시', key: 'TIME_12', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '13시', key: 'TIME_13', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '14시', key: 'TIME_14', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '15시', key: 'TIME_15', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '16시', key: 'TIME_16', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '17시', key: 'TIME_17', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '18시', key: 'TIME_18', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '19시', key: 'TIME_19', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '20시', key: 'TIME_20', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '21시', key: 'TIME_21', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '22시', key: 'TIME_22', width: '100', customBodyCellStyle: 'table-body-style-right' },
  { title: '23시', key: 'TIME_23', width: '100', customBodyCellStyle: 'table-body-style-right' },
]);

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  {
    label: '',
    key: 'WORK_STAT',
    type: 'radio',
    option: [
      { title: '기간별', value: 'WORK_PERIOD' },
      { title: '일별', value: 'WORK_DAY' },
    ],
  },
]);

const setCustomBodyRowStyleSummary = (item) => {
  if (item.LANE_NM === '총합계' || item.LANE_NM === '비율(%)') {
    return 'table-body-style-summary-1';
  } else if (item.LANE_NM.substr(-2) === '소계') {
    return 'table-body-style-summary-2';
  }
  return '';
};
const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  WORK_STAT: 'WORK_PERIOD',
});
watch(
  () => searchData.value.WORK_STAT,
  () => {
    console.log(searchData.value.WORK_STAT);
    searchData.value.WORK_STAT == 'WORK_PERIOD'
      ? (headers.value = [
          { title: '순번', key: 'ROW_NUMBER', width: '100' },
          { title: '차로명', key: 'LANE_NM', width: '150' },
          { title: '합계', key: 'TRF_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '00시', key: 'TIME_00', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '01시', key: 'TIME_01', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '02시', key: 'TIME_02', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '03시', key: 'TIME_03', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '04시', key: 'TIME_04', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '05시', key: 'TIME_05', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '06시', key: 'TIME_06', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '07시', key: 'TIME_07', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '08시', key: 'TIME_08', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '09시', key: 'TIME_09', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '10시', key: 'TIME_10', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '11시', key: 'TIME_11', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '12시', key: 'TIME_12', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '13시', key: 'TIME_13', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '14시', key: 'TIME_14', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '15시', key: 'TIME_15', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '16시', key: 'TIME_16', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '17시', key: 'TIME_17', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '18시', key: 'TIME_18', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '19시', key: 'TIME_19', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '20시', key: 'TIME_20', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '21시', key: 'TIME_21', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '22시', key: 'TIME_22', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '23시', key: 'TIME_23', width: '100', customBodyCellStyle: 'table-body-style-right' },
        ])
      : (headers.value = [
          { title: '순번', key: 'ROW_NUMBER', width: '100' },
          { title: '일자', key: 'TRF_DATE', width: '100' },
          { title: '차로명', key: 'LANE_NM', width: '150' },
          { title: '합계', key: 'TRF_SUM', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '00시', key: 'TIME_00', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '01시', key: 'TIME_01', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '02시', key: 'TIME_02', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '03시', key: 'TIME_03', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '04시', key: 'TIME_04', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '05시', key: 'TIME_05', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '06시', key: 'TIME_06', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '07시', key: 'TIME_07', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '08시', key: 'TIME_08', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '09시', key: 'TIME_09', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '10시', key: 'TIME_10', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '11시', key: 'TIME_11', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '12시', key: 'TIME_12', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '13시', key: 'TIME_13', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '14시', key: 'TIME_14', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '15시', key: 'TIME_15', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '16시', key: 'TIME_16', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '17시', key: 'TIME_17', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '18시', key: 'TIME_18', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '19시', key: 'TIME_19', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '20시', key: 'TIME_20', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '21시', key: 'TIME_21', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '22시', key: 'TIME_22', width: '100', customBodyCellStyle: 'table-body-style-right' },
          { title: '23시', key: 'TIME_23', width: '100', customBodyCellStyle: 'table-body-style-right' },
        ]);
  },
  {
    immediate: true,
  },
);
const trafficLineHourData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/traffic/getTrafficLineHour', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    trafficLineHourData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
  } catch (error) {
    alert(`차로/시간별 교통량 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 1;
  if (trafficLineHourData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value.map((obj) => ({ ...obj, width: obj.title.length * 5 }));
    excelDownload(
      row,
      searchHeader.value,
      searchData.value,
      excelHeaders,
      trafficLineHourData.value,
      '차로시간별 교통량 조회',
      '차로시간별 교통량 조회',
    );
  } else {
    alert(`엑셀다운로드 취소`);
  }
};
const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (trafficLineHourData.value.length === 0) {
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
    jsonData.value = createOzDataset('/TRAFFIC/trafficLineHour.ozr', {
      CSV_DATA: trafficLineHourData.value,
      START_DATE: searchData.value['START_DATE'],
      END_DATE: searchData.value['END_DATE'],
      TITLE_NM: '차로시간별 교통량 조회',
      IC_CODE: authStore.getIcCode,
      IC_NAME: authStore.getIcNm,
      IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
      ...getCondition(searchHeader.value, ozSearchData.value),
    });
    isActive.value = true;
  } catch (error) {
    alert(`차로/시간별 교통량 조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
