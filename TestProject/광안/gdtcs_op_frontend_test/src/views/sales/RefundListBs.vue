<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current">
      <template v-slot:header_btn>
        <InformationComponent message="부산면제 및 공차택시 감면심사에 따른 환불대상입니다." icon-type="information" />
      </template>
    </SearchDataComponent>
  </div>
  <TableComponent
    scroll-key="RefundListBsScroll"
    :headers="mainHeaders"
    :contents="mainContents"
    :height-offset="heightOffset"
    :footer-contents="footerContents"
  >
  </TableComponent>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { request, btnHandler, showMessage, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';

const appTitle = `환불대상 조회(부산면제)`;
const isLoading = ref(false);
const authStore = useAuthStore();

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 36 + 29 + 30;

const searchHeader = ref([
  { label: '조회기간', key: 'START_DATE', type: 'date', dateType: 'day' },
  { label: '~', key: 'END_DATE', type: 'date', dateType: 'day' },
  { label: '카드번호', key: 'CARD_NO', type: 'input', width: '110px', valid: 'digit' },
  { label: '차량번호', key: 'CAR_NO', type: 'input', width: '110px', valid: 'digit|korean' },
]);

const searchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    CARD_NO: '',
    CAR_NO: '',
  },
  prev: {},
});

const mainHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE_DP', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '일련번호', key: 'HAND_SNO', width: '70' },
  { title: '발생시각', key: 'HAND_DT_DP', width: '150' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '150' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '85', customBodyCellStyle: 'table-body-style-right' },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '70', customBodyCellStyle: 'table-body-style-right' },
  { title: '출금액', key: 'WTHD_FARE_DP', width: '70', customBodyCellStyle: 'table-body-style-right' },
  // { title: '환급금액', key: 'RFND_FARE_DP', width: '90', customBodyCellStyle: 'table-body-style-right' },
  { title: '카드종류', key: 'ECARD_TYPE_DP', width: '90' },
  { title: '면제구분', key: 'BS_EXM_TYPE_DTL_DP', width: '80' },
  { title: '차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '전자카드번호', key: 'CARD_NO_DP', width: '150' },
  { title: '공차조회결과', key: 'TAXI_EXM_TYPE_DP', width: '95' },
  { title: '공차조회시각', key: 'TRNRCP_DT_DP', width: '150' },
  // { title: 'OBU번호', key: 'OBU_NO_DP', width: '150' },
  { title: '비고', key: 'NOTE', width: '200', customBodyCellStyle: 'table-body-style-left' },
]);

const mainContents = ref([]);

const subTotalCount = ref('0');
const subTotalSumOriginPassFare = ref('0');
const subTotalSumPassFare = ref('0');
const subTotalSumWthdFare = ref('0');

const footerContents = ref([
  { title: '총 건수', value: subTotalCount, unit: '건' },
  { title: '원통행요금', value: subTotalSumOriginPassFare, unit: '원' },
  { title: '통행요금', value: subTotalSumPassFare, unit: '원' },
  { title: '출금액', value: subTotalSumWthdFare, unit: '원' },
]);

const setFooterContents = () => {
  subTotalCount.value = mainContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumOriginPassFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumPassFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumWthdFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.WTHD_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const handleSearch = async () => {
  try {
    isLoading.value = true;
    const response = await request('post', 'api/sales/getRefundListBs', {
      ...searchData.current,
      ...{
        IC_CODE: authStore.getIcCode,
        START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
        END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
        LOC_CO_DIV: '1',
      },
    });
    searchData.prev = getCondition(searchHeader.value, searchData.current);
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
    excelDownload(headerRow, searchHeader.value, searchData.prev, mainHeaders.value, mainContents.value, appTitle, appTitle, [
      footerContents.value
        .map((item) => {
          return `${item.title}: ${item.value} ${item.unit}`;
        })
        .join(', '),
    ]);
  } else {
    showMessage(`엑셀다운로드를 취소했습니다.`);
  }
};

btnHandler({
  Search: handleSearch,
  Excel: handleExcel,
});
</script>
