<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current">
      <template v-slot:header_btn>
        <InformationComponent message="통합복지카드(위치기반) 감면심사에 따른 환불대상입니다." icon-type="information" />
      </template>
    </SearchDataComponent>
  </div>
  <TableComponent
    scroll-key="RefundListExScroll"
    :headers="mainHeaders"
    :contents="mainContents"
    :footer-contents="footerContents"
    :height-offset="heightOffset"
    row-type="1"
  >
  </TableComponent>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { request, btnHandler, showMessage, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';

const appTitle = `환불대상 조회(통합복지)`;
const isLoading = ref(false);
const authStore = useAuthStore();

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 36 + 29 + 30;

const selectOptHandType = [
  { value: '', title: '전체' },
  { value: '2', title: '자동처리' },
  { value: '3', title: '수동처리' },
];

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '차량번호', key: 'INST_CAR_NO', type: 'input', width: '110px', valid: 'digit|korean' },
  { label: '처리구분', key: 'EXMT_HAND_TYPE', type: 'select', option: selectOptHandType, width: '80px' },
]);

const searchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    INST_CAR_NO: '',
    EXMT_HAND_TYPE: '',
  },
  prev: {},
});

const mainHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '90', excelWidth: 20 },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90', excelWidth: 20 },
  { title: '근무번호', key: 'WORK_NO', width: '90', excelWidth: 20 },
  { title: '일련번호', key: 'HAND_SNO', width: '90', excelWidth: 20 },
  { title: '발생시각', key: 'OCC_DT_DP', width: '150', excelWidth: 20 },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '120', excelWidth: 20 },
  { title: '차종', key: 'CAR_TYPE_DP', width: '90', excelWidth: 20 },
  { title: '전자카드', key: 'ECARD_NO_DP', width: '150', excelWidth: 20 },
  { title: 'OBU번호', key: 'OBU_NO_DP', width: '150', excelWidth: 20 },
  { title: '차량번호', key: 'INST_CAR_NO', width: '110', excelWidth: 20 },
  { title: '카드종류', key: 'ECARD_TYPE_DP', width: '90', excelWidth: 20 },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '90', excelWidth: 20 },
  { title: '수납요금', key: 'PAY_FARE_DP', width: '90', excelWidth: 20 },
  { title: '환급금액', key: 'RFND_FARE_DP', width: '90', excelWidth: 20 },
  { title: '처리구분', key: 'EXMT_HAND_TYPE_DP', width: '90', excelWidth: 20 },
  { title: '비고', key: 'NOTE', width: '150', excelWidth: 20 },
]);

const mainContents = ref([]);

const subTotalCount = ref('0');
const subTotalSumPassFare = ref('0');
const subTotalSumRefundFare = ref('0');

const footerContents = ref([
  { title: '총 건수', value: subTotalCount, unit: '건' },
  { title: '통행요금', value: subTotalSumPassFare, unit: '원' },
  { title: '환급금액', value: subTotalSumRefundFare, unit: '원' },
]);

const setFooterContents = () => {
  subTotalCount.value = mainContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumPassFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumRefundFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.RFND_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/sales/getRefundList', {
      ...searchData.current,
      ...{
        IC_CODE: authStore.getIcCode,
        START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
        END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
        LOC_CO_DIV: '0',
      },
    });
    searchData.prev = { ...searchData.current };
    mainContents.value = response;
    setFooterContents();
    if (response.length === 0) {
      showMessage('조회된 데이터가 없습니다.', 'warning');
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
