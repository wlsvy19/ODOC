<template>
  <SearchDataComponent :headers="searchHeader" v-model="searchData" @keyup.enter="handleSearch" />
  <TableComponent
    :heightOffset="286"
    rowType="mix"
    scrollKey="trafficHourData"
    :headers="headers"
    :contents="trafficHourData"
    :custom-body-row-style="setCustomBodyRowStyleSummary"
  />
  <OZReportDialog v-model:isActive="isActive" v-model:jsonData="jsonData" />
  <LoadingComponent v-if="isLoading" />
</template>

<script setup>
import { ref, nextTick, computed, watch } from 'vue';
import { request, btnHandler, createOzDataset, getCondition } from '@/utils/common';
import { excelDownload } from '@/utils/excel';
import dayjs from 'dayjs';
import { useAuthStore, ozAppImageUrl } from '@/stores/index';

import { useRoute } from 'vue-router';
const route = useRoute();
const appCode = computed(() => route.path).value.replace('/', '');

const authStore = useAuthStore();
const isActive = ref(false);
const jsonData = ref('');
const isLoading = ref(false);

const headers = ref([]);

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '조회시간', key: 'START_SECOND', type: 'inputButton', max: 24 },
  { label: '~', key: 'END_SECOND', type: 'inputButton', max: 24 },
  {
    label: '',
    key: 'TRAFFIC_HOUR_DIV',
    type: 'radio',
    option: [
      { title: '전체', value: 'ALL' },
      { title: '상하행 구분', value: 'UP_DOWN_DIV' },
    ],
  },
]);
const setCustomBodyRowStyleSummary = (item) => {
  if (item.TRF_HR === '총합계') {
    return 'table-body-style-summary-1';
  } else if (item.TRF_HR.substr(-2) === '합계') {
    return 'table-body-style-summary-2';
  }
  return '';
};

const searchData = ref({
  START_DATE: dayjs().format('YYYY-MM-DD'),
  END_DATE: dayjs().format('YYYY-MM-DD'),
  START_SECOND: 0,
  END_SECOND: 24,
  TRAFFIC_HOUR_DIV: 'ALL',
});
watch(
  () => searchData.value.TRAFFIC_HOUR_DIV,
  () => {
    console.log(searchData.value.TRAFFIC_HOUR_DIV);
    searchData.value.TRAFFIC_HOUR_DIV == 'UP_DOWN_DIV'
      ? (headers.value = [
          {
            title: '구분',
            children: [
              { title: '방향', key: 'CHRO', width: '100' },
              { title: '근무시간', key: 'TRF_HR', width: '100' },
            ],
          },
          {
            title: '차종구분',
            children: [
              { title: '소계', key: 'CARTYPE_TOTAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
              { title: '경형', key: 'LIGHT_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
              { title: '소형', key: 'SMALLSIZE_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
              { title: '중형', key: 'FULLSIZED_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
              { title: '대형', key: 'SPECIAL_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
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
        ])
      : (headers.value = [
          {
            title: '구분',
            children: [{ title: '근무시간', key: 'TRF_HR', width: '100' }],
          },
          {
            title: '차종구분',
            children: [
              { title: '소계', key: 'CARTYPE_TOTAL', width: '100', customBodyCellStyle: 'table-body-style-right' },
              { title: '경형', key: 'LIGHT_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
              { title: '소형', key: 'SMALLSIZE_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
              { title: '중형', key: 'FULLSIZED_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
              { title: '대형', key: 'SPECIAL_CAR', width: '100', customBodyCellStyle: 'table-body-style-right' },
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
  },
  {
    immediate: true,
  },
);
const trafficHourData = ref([]);
const ozSearchData = ref([]);

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const data = await request('post', 'api/traffic/getTrafficHour', {
      ...searchData.value,
      ...{
        START_DATE: searchData.value['START_DATE'].replaceAll('-', ''),
        END_DATE: searchData.value['END_DATE'].replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });

    trafficHourData.value = data;
    ozSearchData.value = JSON.parse(JSON.stringify(searchData.value));

    if (data.length == 0) {
      alert(`데이터가 없습니다.`);
    }
  } catch (error) {
    alert(`시간별 교통량 조회 중 에러가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
  } finally {
    isLoading.value = false;
  }
};

const handleExcel = () => {
  const row = 'mix';
  if (trafficHourData.value.length === 0) {
    alert(`조회된 데이터가 없습니다.`);
    return;
  }
  if (JSON.stringify(ozSearchData.value) !== JSON.stringify(searchData.value)) {
    alert(`조회조건이 변경됐습니다. 조회 후 시도해주세요.`);
    return;
  }
  if (confirm(`엑셀 다운로드를 진행하시겠습니까?`)) {
    const excelHeaders = headers.value;

    excelDownload(row, searchHeader.value, searchData.value, excelHeaders, trafficHourData.value, '시간별 교통량 조회', '시간별 교통량 조회');
  } else {
    alert(`엑셀다운로드 취소`);
  }
};

const handlePrint = async () => {
  try {
    isLoading.value = true;
    if (trafficHourData.value.length === 0) {
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
    if (searchData.value['TRAFFIC_HOUR_DIV'] == 'ALL') {
      jsonData.value = createOzDataset('/TRAFFIC/trafficHourAll.ozr', {
        CSV_DATA: trafficHourData.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        TITLE_NM: '시간별 교통량 조회-전체',
        IC_CODE: authStore.getIcCode,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        IC_NAME: authStore.getIcNm,
        ...getCondition(searchHeader.value, ozSearchData.value),
      });
    } else {
      jsonData.value = createOzDataset('/TRAFFIC/trafficHourDiv.ozr', {
        CSV_DATA: trafficHourData.value,
        START_DATE: searchData.value['START_DATE'],
        END_DATE: searchData.value['END_DATE'],
        TITLE_NM: '시간별 교통량 조회-상하행구분',
        IC_CODE: authStore.getIcCode,
        IC_NAME: authStore.getIcNm,
        IMAGE_URL: ozAppImageUrl + imagePath.RPT_APP_CODE,
        ...getCondition(searchHeader.value, ozSearchData.value),
      });
    }
    isActive.value = true;
  } catch (error) {
    alert(`시간별 교통량 조회 Report Error가 발생했습니다.\n문제가 지속된다면 관리자한테 문의하세요.`);
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
