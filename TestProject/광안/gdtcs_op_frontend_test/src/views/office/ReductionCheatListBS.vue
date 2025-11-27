<template>
  <LoadingComponent v-if="isLoading" />
  <div @keyup.enter="handleSearch">
    <SearchDataComponent :headers="searchHeader" v-model="searchData.current" />
  </div>
  <TableComponent
    scroll-key="ReductionCheatListBS"
    :headers="mainHeaders"
    :contents="mainContents"
    :height-offset="heightOffset"
    :footer-contents="footerContents"
  />
</template>

<script setup>
import { ref, onActivated, nextTick, reactive } from 'vue';
import { request, btnHandler, getLaneNo, showMessage, getSystemSmallCode, getCondition } from '@/utils/common';
import dayjs from 'dayjs';
import { useAuthStore } from '@/stores';
import { excelDownload } from '@/utils/excel';

//#region Common
const appTitle = `감면 부정사용내역(부산)`;
const isLoading = ref(false);
const authStore = useAuthStore();

/* Base: 189
 * Search Header: 36
 * Grid Header: 29
 * Grid Footer: 30
 */
const heightOffset = 189 + 36 + 29 + 30;
//#endregion

//#region Select Box Option
/* Select Box Option - search */
const selectOptLaneNo = ref([]);
const selectOptBsExmTypeDtl = ref([]);

onActivated(async () => {
  selectOptLaneNo.value = await getLaneNo();
  selectOptBsExmTypeDtl.value = getSystemSmallCode('292', true);
  await nextTick();
});
//#endregion

//#region Search
const searchHeader = ref([
  { label: '조회기간:', key: 'START_DATE', type: 'date' },
  { label: '~', key: 'END_DATE', type: 'date' },
  { label: '차로번호', key: 'LANE_NO', type: 'select', option: selectOptLaneNo, width: '95px' },
  { label: '부정발생구분', key: 'BS_EXM_TYPE_DTL', type: 'select', option: selectOptBsExmTypeDtl, width: '130px' },
  { label: '처리차량번호', key: 'CAR_NO', type: 'input', width: '110px' },
]);

const searchData = reactive({
  current: {
    START_DATE: dayjs().format('YYYY-MM-DD'),
    END_DATE: dayjs().format('YYYY-MM-DD'),
    LANE_NO: '',
    BS_EXM_TYPE_DTL: '',
    CAR_NO: '',
  },
  prev: {},
});
//#endregion

//#region Main Grid
const mainHeaders = ref([
  { title: '순번', key: 'ROW_NUMBER', width: '70' },
  { title: '근무일자', key: 'WORK_DATE', width: '90' },
  { title: '근무번호', key: 'WORK_NO', width: '70' },
  { title: '처리번호', key: 'HAND_SNO', width: '70' },
  { title: '발생일시', key: 'HAND_DT_DP', width: '150' },
  { title: '처리차종', key: 'HAND_CAR_TYPE_DP', width: '80' },
  { title: '심사구분', key: 'BS_EXM_TYPE_DIV_DP', width: '80' },
  { title: '원통행요금', key: 'ORIGIN_PASS_FARE_DP', width: '90' },
  { title: '통행요금', key: 'PASS_FARE_DP', width: '90' },
  { title: '수납금액', key: 'PAY_FARE_DP', width: '90' },
  { title: '카드번호', key: 'CARD_NO_DP', width: '160' },
  { title: '위반코드', key: 'VLTN_CODE_DP', width: '120' },
  { title: '처리차량번호', key: 'HAND_CAR_NO', width: '110' },
  { title: '발생구분', key: 'BS_EXM_TYPE_DTL_DP', width: '90' },
]);

const mainContents = ref([]);

const subTotalCount = ref('0');
const subTotalSumOriginFare = ref('0');
const subTotalSumPayFare = ref('0');

const footerContents = ref([
  { title: '총 건수', value: subTotalCount, unit: '건' },
  { title: '원통행요금', value: subTotalSumOriginFare, unit: '원' },
  { title: '수납요금', value: subTotalSumPayFare, unit: '원' },
]);

const setFooterContents = () => {
  subTotalCount.value = mainContents.value.length.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumOriginFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.ORIGIN_PASS_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  subTotalSumPayFare.value = mainContents.value
    .reduce((acc, item) => {
      return acc + item.PAY_FARE;
    }, 0)
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};
//#endregion

//#region Main Button
const handleSearch = async () => {
  isLoading.value = true;
  try {
    const response = await request('post', 'api/office/getCheatReductionBsList', {
      ...searchData.current,
      ...{
        START_DATE: searchData.current.START_DATE.replaceAll('-', ''),
        END_DATE: searchData.current.END_DATE.replaceAll('-', ''),
        IC_CODE: authStore.getIcCode,
      },
    });
    searchData.prev = getCondition(searchHeader.value, searchData.current);
    mainContents.value = response;
    setFooterContents();
  } catch (error) {
    showMessage(error, 'warning');
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
//#endregion
</script>

<style scoped></style>
